package com.dongyang.dongpo.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class OAuth2Attribute {
    
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String provider;
    
    public static OAuth2Attribute createAttribute(String provider, String attributeKey, Map<String, Object> attributes){
        switch (provider){
            case "KaKao":
                return kakao(provider, "email", attributes);
        }
    }

    private OAuth2Attribute kakao(String provider, String attributeKey,
                                  Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .provider(provider)
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();

    }

    public Map<String, Object> converToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}
