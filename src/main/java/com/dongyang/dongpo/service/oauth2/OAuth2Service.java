package com.dongyang.dongpo.service.oauth2;


import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.service.member.SignService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuth2Service {

    private final SignService signService;
    private final MemberRepository memberRepository;

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_url}")
    private String kakaoRedirectUrl;


    public void kakaoCallBack(String code) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com/oauth/token")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUrl);
        params.add("code", code);

        Mono<String> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParams(params)
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        response.map(responseBody -> {
            JSONObject jsonObject = new JSONObject(responseBody);
            String accessToken = jsonObject.getString("access_token");
            getKakaoUserInfo(accessToken);
        });
    }

    public void getKakaoUserInfo(String accessToken){
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .defaultHeader("Authorization", "Bearer "+accessToken)
                .build();

        webClient.post()
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
                    JSONObject profile = kakaoAccount.getJSONObject("profile");
                    String email = profile.getString("email"); // 임시
                    validateMember(email);
                })
                .onErrorResume(Throwable::printStackTrace);
    }

    private void validateMember(String email) {
        if (memberRepository.existsByEmail(email))
            signService.SignIn(email);
        else
            signService.SignUp(email);;
    }
}
