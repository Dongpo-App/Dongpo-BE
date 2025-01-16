package com.dongyang.dongpo.common.exception;

import com.dongyang.dongpo.domain.auth.dto.ClaimsResponseDto;
import lombok.Getter;

@Getter
public class CustomSignupException extends CustomException {

    private final ClaimsResponseDto claimsResponseDto;

    public CustomSignupException(ErrorCode errorCode, ClaimsResponseDto claimsResponseDto) {
        super(errorCode);
        this.claimsResponseDto = claimsResponseDto;
    }
}
