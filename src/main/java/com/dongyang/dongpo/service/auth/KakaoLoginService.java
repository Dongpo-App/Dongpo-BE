package com.dongyang.dongpo.service.auth;


import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Member.SocialType;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.service.member.MemberService;
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
public class KakaoLoginService{

    private final MemberService memberService;

    public ResponseEntity getKakaoUserInfo(String accessToken) {
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
}
