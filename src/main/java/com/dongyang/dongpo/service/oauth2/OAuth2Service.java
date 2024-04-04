package com.dongyang.dongpo.service.oauth2;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class OAuth2Service {

    @Value("${kakao.client_id}")
    private String kakaoClientId;

    @Value("${kakao.redirect_url}")
    private String kakaoRedirectUrl;

    public Mono<Mono<String>> kakaoCallBack(String code) {
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

        return response.map(responseBody -> {
            JSONObject jsonObject = new JSONObject(responseBody);
            String accessToken = jsonObject.getString("access_token");
            return getKakaoUserInfo(accessToken);
        });
    }

    public Mono<String> getKakaoUserInfo(String accessToken){
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com/v2/user/me")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .defaultHeader("Authorization", "Bearer "+accessToken)
                .build();

        return webClient.post()
                .retrieve()
                .bodyToMono(String.class)
                .map(responseBody -> {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
                    JSONObject profile = kakaoAccount.getJSONObject("profile");
                    String nickname = profile.getString("nickname");
                    return nickname;
                })
                .onErrorResume(error -> {
                    error.printStackTrace();
                    return Mono.just("error");
                });
    }
}
