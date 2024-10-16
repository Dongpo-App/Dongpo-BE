package com.dongyang.dongpo.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppleRefreshTokenDto {
    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private String error;
}
