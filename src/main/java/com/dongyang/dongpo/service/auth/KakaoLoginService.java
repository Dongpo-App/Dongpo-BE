package com.dongyang.dongpo.service.auth;


import com.dongyang.dongpo.domain.member.SocialType;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_url}")
    private String redirectUrl;


    public ResponseEntity kakaoCallBack(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);

        String responseBody = webClient.post()
                .uri(uriBuilder -> uriBuilder.build())
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking call to wait for response

        JSONObject jsonObject = new JSONObject(responseBody);
        String accessToken = jsonObject.getString("access_token");
        return getKakaoUserInfo(accessToken);
    }



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

        memberRegValidate(email);
        return memberService.socialSave(UserInfo.builder()
                .id(id)
                .email(email)
                .provider(SocialType.KAKAO)
                .build());
    }

    private void memberRegValidate(String email){
        if (memberRepository.existsByEmail(email)){
            // token 발급
        }else {

        }
    }
}
