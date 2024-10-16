package com.dongyang.dongpo.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppleLoginDto {
    private String identityToken;
    private String authorizationCode;
}
