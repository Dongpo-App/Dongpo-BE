package com.dongyang.dongpo.service.oauth2;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class OAuth2Service {

    @Value("${kakao.reg.client_id}")
    private String kakaoClientId;

    @Value("${kakao.reg.redirect_url}")
    private String kakaoRedirectUrl;



    public String kakaoCallBack(String code) throws JsonProcessingException {
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

        response.subscribe(
                responseBody -> {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String accessToken = jsonObject.getString("access_token");
                    System.out.println("Access Token: " + accessToken);
                },
                error -> {

                }
        );

        return "a";
    }
}
