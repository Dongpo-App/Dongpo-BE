package com.dongyang.dongpo.domain.auth.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.common.util.auth.AppleKeyGenerator;
import com.dongyang.dongpo.common.util.auth.ApplePublicKeyGenerator;
import com.dongyang.dongpo.common.util.auth.ApplePublicKeyResponse;
import com.dongyang.dongpo.common.util.auth.AppleTokenParser;
import com.dongyang.dongpo.domain.auth.dto.*;
import com.dongyang.dongpo.domain.auth.entity.AppleRefreshToken;
import com.dongyang.dongpo.domain.auth.repository.AppleRefreshTokenRepository;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.enums.Gender;
import com.dongyang.dongpo.domain.member.enums.SocialType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocialPlatformService {

    private static final String APPLE_AUTH_URL = "https://appleid.apple.com";
    private static final String KAKAO_OAUTH_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_API_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_TERMS_API_URL = "https://kapi.kakao.com/v2/user/service_terms";

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.terms_tag.service_terms}")
    private String serviceTermsTag;

    @Value("${kakao.terms_tag.marketing_terms}")
    private String marketingTermsTag;

    @Value("${apple.client.id}")
    private String appleClientId;

    private final AppleTokenParser appleTokenParser;
    private final AppleKeyGenerator appleKeyGenerator;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleRefreshTokenRepository appleRefreshTokenRepository;

    // 카카오 OAuth 토큰 발급
    public String kakaoCallback(String accessCode) {
        WebClient webClient = WebClient.builder()
                .baseUrl(KAKAO_OAUTH_URL)
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", accessCode);

        String responseBody = webClient.post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = new JSONObject(responseBody);
        return jsonObject.getString("access_token");
    }

    // 카카오 사용자 정보 조회
    public UserInfo getKakaoUserInfo(String accessToken) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(KAKAO_USER_API_URL)
                    .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .defaultHeader("Authorization", "Bearer " + accessToken)
                    .build();

            String responseBody = webClient.post()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
            JSONObject profile = kakaoAccount.getJSONObject("profile");

            String email = kakaoAccount.getString("email");
            String id = String.valueOf(jsonObject.getLong("id"));
            String birthyear = kakaoAccount.getString("birthyear");
            String birthday = kakaoAccount.getString("birthday").substring(0, 2) + "-" + kakaoAccount.getString("birthday").substring(2, 4);
            String nickname = profile.getString("nickname");
            Gender gender;

            if (kakaoAccount.getString("gender").equals("female"))
                gender = Gender.GEN_FEMALE;
            else if (kakaoAccount.getString("gender").equals("male"))
                gender = Gender.GEN_MALE;
            else
                gender = Gender.NONE;

            // 간편 가입 과정에서 동의한 서비스 이용 약관 조회
            Map<String, Boolean> termsAgreementInfo = getServiceTermsAgreementInfoFromKakao(accessToken);

            return UserInfo.builder()
                    .socialId(id)
                    .email(email)
                    .nickname(nickname)
                    .birthyear(birthyear)
                    .birthday(birthday)
                    .gender(gender)
                    .provider(SocialType.KAKAO)
                    .isServiceTermsAgreed(termsAgreementInfo.get(serviceTermsTag))
                    .isMarketingTermsAgreed(termsAgreementInfo.get(marketingTermsTag))
                    .build();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401)
                throw new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID);
            else
                throw e;
        }
    }

    // 사용자의 서비스 이용 약관 동의 여부 조회
    private Map<String, Boolean> getServiceTermsAgreementInfoFromKakao(String accessToken) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl(KAKAO_TERMS_API_URL)
                    .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .defaultHeader("Authorization", "Bearer " + accessToken)

                    .build();

            String responseBody = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("result", "app_service_terms")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONArray serviceTerms = new JSONObject(responseBody).getJSONArray("service_terms");
            return IntStream.range(0, serviceTerms.length())
                    .mapToObj(serviceTerms::getJSONObject)
                    .collect(Collectors.toMap(
                            term -> term.getString("tag"),
                            term -> term.getBoolean("agreed")
                    ));
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401)
                throw new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID);
            else
                throw e;
        }
    }

    /*
    public JwtToken getNaverUserInfo(String accessToken) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://openapi.naver.com/v1/nid/me")
                    .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .defaultHeader("Authorization", "Bearer " + accessToken)
                    .build();

            String responseBody = webClient.post()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject response = jsonObject.getJSONObject("response");

            String email = response.getString("email");
            String birthyear = response.getString("birthyear");
            String birthday = response.getString("birthday");
            String id = response.getString("id");
            String nickname = response.getString("nickname");
            Member.Gender gender;

            if (response.getString("gender").equals("F"))
                gender = Member.Gender.GEN_FEMALE;
            else if (response.getString("gender").equals("M"))
                gender = Member.Gender.GEN_MALE;
            else
                gender = Member.Gender.NONE;

            return memberService.socialSave(UserInfo.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .gender(gender)
                    .birthyear(birthyear)
                    .birthday(birthday)
                    .provider(SocialType.NAVER)
                    .build());
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401)
                throw new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID);
            else
                throw e;
        }
    }
     */

    // 애플 사용자 정보 조회
    public Claims getAppleUserInfo(AppleLoginDto appleLoginDto) {
        // identityToken 파싱 작업 수행
        Map<String, String> header = appleTokenParser.parseHeader(appleLoginDto.getIdentityToken());

        // Apple 인증 서버로부터 공개 키 불러옴
        ApplePublicKeyResponse publicKeys = getApplePublicKeys();

        // 공개 키 중에서 identityToken의 kid, alg 값과 일치하는 키 탐색 후 새로운 공개 키 생성
        PublicKey publicKey = applePublicKeyGenerator.generate(header, publicKeys);

        // identityToken 검증 작업 수행 및 클레임 추출
        Claims claims = validateIdentityToken(appleLoginDto.getIdentityToken(), publicKey);

        // Apple RefreshToken 요청
        String appleRefreshToken = getAppleRefreshToken(appleLoginDto.getAuthorizationCode());

        // Apple RefreshToken 저장
        saveOrUpdateRefreshToken(claims.get("sub", String.class), appleRefreshToken);

        // claims 반환
        return claims;
    }

    // 애플 IdentityToken 검증
    private Claims validateIdentityToken(String identityToken, PublicKey publicKey) {
        // 클레임 추출
        Claims claims = appleTokenParser.extractClaims(identityToken, publicKey);

        // iss : 토큰 발행자 검증
        if (!APPLE_AUTH_URL.equals(claims.getIssuer()))
            throw new CustomException(ErrorCode.TOKEN_ISSUER_MISMATCH);

        // aud : 토큰 수신자 검증
        if (!appleClientId.equals(claims.getAudience()))
            throw new CustomException(ErrorCode.TOKEN_AUDIENCE_MISMATCH);

        // exp : 토큰 만료 여부 검증
        if (claims.getExpiration().before(new Date()))
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);

        return claims;
    }

    // 애플 인증 서버로부터 공개 키 불러오기
    private ApplePublicKeyResponse getApplePublicKeys() {
        WebClient webClient = WebClient.builder()
                .baseUrl(APPLE_AUTH_URL)
                .build();

        return webClient.get()
                .uri("/auth/keys")
                .retrieve()
                .bodyToMono(ApplePublicKeyResponse.class)
                .block();
    }

    // 애플 인증 서버에 RefreshToken 요청
    private String getAppleRefreshToken(String authorizationCode) {
        MultiValueMap<String, String> body = getCreateTokenBody(authorizationCode);

        WebClient webClient = WebClient.builder()
                .baseUrl(APPLE_AUTH_URL)
                .build();

        try {
            AppleTokenResponseDto appleTokenResponse = webClient.post()
                    .uri("/auth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(body))
                    .retrieve()
                    .bodyToMono(AppleTokenResponseDto.class)
                    .block();

            return Objects.requireNonNull(appleTokenResponse).getRefresh_token();
        } catch (WebClientResponseException e) {
            throw new CustomException(ErrorCode.APPLE_AUTHORIZATION_CODE_EXPIRED);
        }
    }

    // 애플 RefreshToken 생성 요청 Body 생성
    private MultiValueMap<String, String> getCreateTokenBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", appleClientId);
        body.add("client_secret", appleKeyGenerator.generateClientSecret());
        body.add("grant_type", "authorization_code");
        return body;
    }

    // 애플 RefreshToken 저장
    public void saveOrUpdateRefreshToken(String socialId, String newRefreshToken) {
        AppleRefreshToken existingToken = appleRefreshTokenRepository.findBySocialId(socialId).orElse(null);

        // 이미 해당 사용자에 대한 RefreshToken이 존재하는 경우 업데이트
        if (existingToken != null) {
            existingToken.updateRefreshToken(newRefreshToken);
        } else { // 혹은 새로운 RefreshToken 저장
            AppleRefreshToken newToken = AppleRefreshToken.builder()
                    .socialId(socialId)
                    .refreshToken(newRefreshToken)
                    .build();
            appleRefreshTokenRepository.save(newToken);
        }
    }

    // 애플 로그인 사용자 탈퇴 메소드
    public void revokeAppleRefreshToken(Member member) {
        AppleRefreshToken refresh = appleRefreshTokenRepository.findBySocialId(member.getSocialId())
                .orElseThrow(() -> new CustomException(ErrorCode.MALFORMED_TOKEN));

        MultiValueMap<String, String> revokeTokenBody = getRevokeTokenBody(refresh.getRefreshToken());

        WebClient webClient = WebClient.builder()
                .baseUrl(APPLE_AUTH_URL)
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
        appleRefreshTokenRepository.deleteBySocialId(member.getSocialId());

        log.info("Member {} Account has been successfully revoked.", member.getEmail());
    }

    // 애플 RefreshToken 폐기 요청 Body 생성
    public MultiValueMap<String, String> getRevokeTokenBody(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", appleClientId);
        body.add("client_secret", appleKeyGenerator.generateClientSecret());
        body.add("token", refreshToken);
        body.add("token_type_hint", "refresh_token");
        return body;
    }
}
