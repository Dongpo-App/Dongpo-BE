package com.dongyang.dongpo.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppleLoginDto {
    private String identityToken;
    private String authorizationCode;
}
