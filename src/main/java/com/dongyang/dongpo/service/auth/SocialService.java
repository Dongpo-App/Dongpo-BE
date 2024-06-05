package com.dongyang.dongpo.service.auth;


import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Member.SocialType;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.SocialTokenDto;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@RequiredArgsConstructor
public class SocialService {

    private final MemberService memberService;

    public JwtToken getKakaoUserInfo(String accessToken) {
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

        String email = kakaoAccount.getString("email");
        String id = String.valueOf(jsonObject.getLong("id"));
        String age = kakaoAccount.getString("age_range");
        Member.Gender gender;

        if (kakaoAccount.getString("gender").equals("female"))
            gender = Member.Gender.GEN_FEMALE;
        else if (kakaoAccount.getString("gender").equals("male"))
            gender = Member.Gender.GEN_MALE;
        else
            gender = Member.Gender.NONE;


        return memberService.socialSave(UserInfo.builder()
                .id(id)
                .email(email)
                .age(age)
                .gender(gender)
                .provider(SocialType.KAKAO)
                .build());
    }


    public JwtToken getNaverUserInfo(String accessToken) {
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
