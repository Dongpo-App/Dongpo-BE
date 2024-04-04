package com.dongyang.dongpo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {

    private String grantType;
    private String clamis;
    private String accessToken;
    private String refreshToken;
}
