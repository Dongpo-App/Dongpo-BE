package com.dongyang.dongpo.service.auth;


import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Member.SocialType;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
public class SocialService {

    private final MemberService memberService;
    private final AppleLoginService appleLoginService;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.terms_tag.service_terms}")
    private String serviceTermsTag;

    @Value("${kakao.terms_tag.marketing_terms}")
    private String marketingTermsTag;

    public JwtToken kakaoCallback(String AccessCode) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", AccessCode);

        String responseBody = webClient.post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = new JSONObject(responseBody);
        String accessToken = jsonObject.getString("access_token");
        return getKakaoUserInfo(accessToken);
    }

    public JwtToken getKakaoUserInfo(String accessToken) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://kapi.kakao.com/v2/user/me")
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
            Member.Gender gender;

            if (kakaoAccount.getString("gender").equals("female"))
                gender = Member.Gender.GEN_FEMALE;
            else if (kakaoAccount.getString("gender").equals("male"))
                gender = Member.Gender.GEN_MALE;
            else
                gender = Member.Gender.NONE;

            // 간편 가입 과정에서 동의한 서비스 이용 약관 조회
            Map<String, Boolean> termsAgreementInfo = getServiceTermsAgreementInfoFromKakao(accessToken);

            return memberService.socialSave(UserInfo.builder()
                    .id(id)
                    .email(email)
                    .nickname(nickname)
                    .birthyear(birthyear)
                    .birthday(birthday)
                    .gender(gender)
                    .provider(SocialType.KAKAO)
                    .isServiceTermsAgreed(termsAgreementInfo.get(serviceTermsTag))
                    .isMarketingTermsAgreed(termsAgreementInfo.get(marketingTermsTag))
                    .build());
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401)
                throw new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID);
            else
                throw e;
        }
    }

    private Map<String, Boolean> getServiceTermsAgreementInfoFromKakao(String accessToken) {
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://kapi.kakao.com/v2/user/service_terms")
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

    public void doLogout(Member member, String authorization) {
        memberService.handleLogout(member, authorization);
    }

    public void doLeave(Member member, String authorization) {
        // 추후 소셜 로그인 플랫폼 추가 시 수정
        switch (member.getSocialType()) {
            case APPLE:
                appleLoginService.revokeToken(member, authorization);
                break;
            case KAKAO:
                memberService.handleLeave(member, authorization);
                break;
//            case NAVER:
//                memberService.handleLeave(member, authorization);
//                break;
            default:
                throw new CustomException(ErrorCode.MALFORMED_TOKEN);
        }
    }
}
