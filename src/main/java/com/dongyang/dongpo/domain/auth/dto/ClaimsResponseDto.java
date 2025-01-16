package com.dongyang.dongpo.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClaimsResponseDto {
    private String socialId;
    private String email;
}
