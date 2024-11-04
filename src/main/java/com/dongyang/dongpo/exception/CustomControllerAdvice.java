package com.dongyang.dongpo.exception;

import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {
    private static final ApiResponse<String> response = new ApiResponse<>();

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> customException(CustomException e) {
        response.setMessage(e.getMessage());
        HttpStatus status = HttpStatus.valueOf(e.getErrorCode().getCode());

        return ResponseEntity.status(status).body(response);
    }

}
