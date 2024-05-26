package com.dongyang.dongpo.service.auth;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Member.SocialType;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NaverLoginService {

    private final MemberService memberService;


    @Value("${naver.client_id}")
    private String clientId;
    @Value("${naver.client_secret}")
    private String clientSecret;


    public ResponseEntity naverCallback(String code, String state) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://nid.naver.com/oauth2.0/token")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("code", code);
        params.add("state", state);

        String responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder.build())
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = new JSONObject(responseBody);
        String accessToken = jsonObject.getString("access_token");
        return getNaverUserInfo(accessToken);
    }

    public ResponseEntity getNaverUserInfo(String accessToken) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://openapi.naver.com/v1/nid/me")
                .defaultHeader("Authorization", "Bearer " + accessToken)
                .build();

        String responseBody = webClient.post()
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject response = jsonObject.getJSONObject("response");

        String email = response.getString("email");
        String age = response.getString("age");
        String id = response.getString("id");
        Member.Gender gender;

        if (response.getString("gender").equals("F"))
            gender = Member.Gender.GEN_FEMALE;
        else if (response.getString("gender").equals("M"))
            gender = Member.Gender.GEN_MALE;
        else
            gender = Member.Gender.NONE;

        return memberService.socialSave(UserInfo.builder()
                .id(id)
                .gender(gender)
                .age(age)
                .email(email)
                .provider(SocialType.NAVER)
                .build());
    }
}
