package com.dongyang.dongpo.service.auth;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.util.auth.ApplePublicKeyResponse;
import com.dongyang.dongpo.util.auth.AppleTokenParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleLoginService {

    private final AppleTokenParser appleTokenParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final MemberService memberService;

    @Value("${apple.team.id}")
    private String appleTeamId;

    @Value("${apple.login.key}")
    private String appleLoginKey;

    @Value("${apple.client.id}")
    private String appleClientId;

    @Value("${apple.redirect.uri}")
    private String appleRedirectUri;

    @Value("${apple.key.path}")
    private String appleKeyPath;

    private final static String appleAuthUrl = "https://appleid.apple.com";

    public JwtToken getAppleUserInfo(String identityToken) {
        // identityToken 파싱 작업 수행
        Map<String, String> header = appleTokenParser.parseHeader(identityToken);

        // Apple 인증 서버로부터 공개 키 불러옴
        ApplePublicKeyResponse publicKeys = getApplePublicKeys();

        // 공개 키 중에서 identityToken의 kid, alg 값과 일치하는 키 탐색 후 새로운 공개 키 생성
        PublicKey publicKey = applePublicKeyGenerator.generate(header, publicKeys);

        // identityToken 검증 작업 수행 및 클레임 추출
        Claims claims = validateIdentityToken(identityToken, publicKey);

        return memberService.socialSave(UserInfo.builder()
                .id(claims.get("sub", String.class))
                .email(claims.get("email", String.class))
                .name("TEMP_NAME") // 임시
                .nickname("TEMP_NICKNAME") // 임시
                .age("20~29") // 임시
                .gender(Member.Gender.GEN_MALE) // 임시
                .provider(Member.SocialType.APPLE)
                .build());
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
}
