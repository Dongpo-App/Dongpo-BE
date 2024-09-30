package com.dongyang.dongpo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String claims;
    private String accessToken;
    private String refreshToken;
}
