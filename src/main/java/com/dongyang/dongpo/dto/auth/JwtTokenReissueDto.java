package com.dongyang.dongpo.dto.auth;

import lombok.Getter;

@Getter
public class JwtTokenReissueDto {
    private String refreshToken;
}
