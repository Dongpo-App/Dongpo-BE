package com.dongyang.dongpo.common.exception;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> customException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        return ResponseEntity.status(status).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }

}
