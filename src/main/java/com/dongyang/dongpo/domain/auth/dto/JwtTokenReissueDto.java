package com.dongyang.dongpo.domain.auth.dto;

import lombok.Getter;

@Getter
public class JwtTokenReissueDto {
    private String refreshToken;
}
