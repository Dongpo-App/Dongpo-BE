package com.dongyang.dongpo.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.jsonwebtoken.Claims;
import lombok.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppleLoginResponse {
    private ClaimsResponse claims;
    private JwtToken jwtToken;

    @Getter
    @Builder
    @JsonSerialize
    @JsonDeserialize
    private static class ClaimsResponse {
        private String socialId;
        private String email;
    }

    public static AppleLoginResponse responseClaims(Claims claims) {
        return AppleLoginResponse.builder()
                .claims(ClaimsResponse.builder()
                        .socialId(claims.get("sub", String.class))
                        .email(claims.get("email", String.class))
                        .build())
                .build();
    }

    public static AppleLoginResponse responseJwtToken(JwtToken jwtToken) {
        return AppleLoginResponse.builder()
                .jwtToken(jwtToken)
                .build();
    }
}
