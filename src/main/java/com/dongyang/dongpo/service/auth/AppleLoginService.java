package com.dongyang.dongpo.service.auth;

import com.dongyang.dongpo.domain.auth.AppleRefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.AppleLoginDto;
import com.dongyang.dongpo.dto.auth.AppleRefreshTokenDto;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.auth.AppleRefreshTokenRepository;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.util.auth.AppleKeyGenerator;
import com.dongyang.dongpo.util.auth.ApplePublicKeyGenerator;
import com.dongyang.dongpo.util.auth.ApplePublicKeyResponse;
import com.dongyang.dongpo.util.auth.AppleTokenParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleLoginService {

    private final AppleTokenParser appleTokenParser;
    private final AppleKeyGenerator appleKeyGenerator;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleRefreshTokenRepository appleRefreshTokenRepository;
    private final MemberService memberService;

    @Value("${apple.client.id}")
    private String appleClientId;

    private final static String appleAuthUrl = "https://appleid.apple.com";

    @Transactional
    public JwtToken getAppleUserInfo(AppleLoginDto appleLoginDto) {
        // identityToken 파싱 작업 수행
        Map<String, String> header = appleTokenParser.parseHeader(appleLoginDto.getIdentityToken());

        // Apple 인증 서버로부터 공개 키 불러옴
        ApplePublicKeyResponse publicKeys = getApplePublicKeys();

        // 공개 키 중에서 identityToken의 kid, alg 값과 일치하는 키 탐색 후 새로운 공개 키 생성
        PublicKey publicKey = applePublicKeyGenerator.generate(header, publicKeys);

        // identityToken 검증 작업 수행 및 클레임 추출
        Claims claims = validateIdentityToken(appleLoginDto.getIdentityToken(), publicKey);

        String appleRefreshToken = getAppleRefreshToken(appleLoginDto.getAuthorizationCode());

        JwtToken jwtToken = memberService.socialSave(UserInfo.builder()
                .id(claims.get("sub", String.class))
                .email(claims.get("email", String.class))
                .name("TEMP_NAME") // 임시
                .nickname("TEMP_NICKNAME") // 임시
                .age("20~29") // 임시
                .gender(Member.Gender.GEN_MALE) // 임시
                .provider(Member.SocialType.APPLE)
                .build());

        Member claimingMember = memberService.findByEmail(claims.get("email", String.class));

        saveOrUpdateRefreshToken(claimingMember, appleRefreshToken);

        return jwtToken;
    }

    private Claims validateIdentityToken(String identityToken, PublicKey publicKey) {
        // 클레임 추출
        Claims claims = appleTokenParser.extractClaims(identityToken, publicKey);

        // iss : 토큰 발행자 검증
        if (!appleAuthUrl.equals(claims.getIssuer()))
            throw new CustomException(ErrorCode.MALFORMED_TOKEN);

        // aud : 토큰 수신자 검증
        if (!appleClientId.equals(claims.getAudience()))
            throw new CustomException(ErrorCode.MALFORMED_TOKEN);

        // exp : 토큰 만료 여부 검증
        if (claims.getExpiration().before(new Date()))
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);

        return claims;
    }

    private ApplePublicKeyResponse getApplePublicKeys() {
        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        return webClient.get()
                .uri("/auth/keys")
                .retrieve()
                .bodyToMono(ApplePublicKeyResponse.class)
                .block();
    }

    // Apple 인증 서버에 RefreshToken 요청
    private String getAppleRefreshToken(String authorizationCode) {
        MultiValueMap<String, String> body = getCreateTokenBody(authorizationCode);

        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        try {
            AppleRefreshTokenDto appleTokenResponse = webClient.post()
                    .uri("/auth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(body))
                    .retrieve()
                    .bodyToMono(AppleRefreshTokenDto.class)
                    .block();

            return Objects.requireNonNull(appleTokenResponse).getRefresh_token();
        } catch (WebClientResponseException e) {
            throw new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID);

        }
    }

    private MultiValueMap<String, String> getCreateTokenBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", appleClientId);
        body.add("client_secret", appleKeyGenerator.generateClientSecret());
        body.add("grant_type", "authorization_code");
        return body;
    }

    @Transactional
    public void saveOrUpdateRefreshToken(Member member, String newRefreshToken) {
        AppleRefreshToken existingToken = appleRefreshTokenRepository.findByMember(member).orElse(null);

        // 이미 해당 사용자에 대한 RefreshToken이 존재하는 경우 업데이트
        if (existingToken != null) {
            existingToken.updateRefreshToken(newRefreshToken);
        } else { // 혹은 새로운 RefreshToken 저장
            AppleRefreshToken newToken = AppleRefreshToken.builder()
                    .member(member)
                    .refreshToken(newRefreshToken)
                    .build();
            appleRefreshTokenRepository.save(newToken);
        }
    }

    // 애플 로그인 사용자 탈퇴 메소드
    @Transactional
    public void revokeToken(Member member) {
        AppleRefreshToken refresh = appleRefreshTokenRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MALFORMED_TOKEN));

        MultiValueMap<String, String> revokeTokenBody = getRevokeTokenBody(refresh.getRefreshToken());

        WebClient webClient = WebClient.builder()
                .baseUrl(appleAuthUrl)
                .build();

        // RefreshToken revoke 요청
        webClient.post()
                .uri("/auth/revoke")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(revokeTokenBody))
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        // DB 속 해당 사용자의 RefreshToken 삭제
        appleRefreshTokenRepository.deleteByMember(member);

        // 사용자 상태를 탈퇴로 변경
        memberService.findByEmail(member.getEmail()).setMemberStatusLeave();

//        // 사용자 상태를 탈퇴로 변경
//        member.setMemberStatusLeave();

        log.info("Member {} Account has been successfully revoked.", member.getEmail());
    }

    public MultiValueMap<String, String> getRevokeTokenBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", appleClientId);
        body.add("client_secret", appleKeyGenerator.generateClientSecret());
        body.add("token", refreshToken);
        body.add("token_type_hint", "refresh_token");
        return body;
    }
}
