package com.dongyang.dongpo.service.auth;

import com.dongyang.dongpo.dto.auth.UserInfo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverLoginService {

    @Value("${naver.client_id}")
    private String clientId;
    @Value("${naver.client_secret}")
    private String clientSecret;
    @Value("${naver.redirect_url}")
    private String redirectUrl;


    public UserInfo naverCallback(String code, String state) {
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

    public UserInfo getNaverUserInfo(String accessToken) {
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
        String id = response.getString("id");

        return UserInfo.builder()
                .id(id)
                .email(email)
                .build();
    }
}
