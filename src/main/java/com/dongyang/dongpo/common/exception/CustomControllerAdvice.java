package com.dongyang.dongpo.common.exception;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.auth.dto.ClaimsResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> customException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        return ResponseEntity.status(status).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(CustomSignupException.class)
    public ResponseEntity<ApiResponse<ClaimsResponseDto>> customException(CustomSignupException e) {
        ErrorCode errorCode = e.getErrorCode();

        HttpStatus status = HttpStatus.valueOf(errorCode.getStatus());
        return ResponseEntity.status(status).body(
                new ApiResponse<>(e.getClaimsResponseDto(), errorCode.getCode(), errorCode.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.ARGUMENT_NOT_SATISFIED;
        return ResponseEntity.status(errorCode.getStatus()).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> constraintViolationException(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.ARGUMENT_NOT_SATISFIED;
        return ResponseEntity.status(errorCode.getStatus()).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.ARGUMENT_NOT_SATISFIED;
        return ResponseEntity.status(errorCode.getStatus()).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<String>> handlerMethodValidationException(HandlerMethodValidationException e) {
        ErrorCode errorCode = ErrorCode.ARGUMENT_NOT_SATISFIED;
        return ResponseEntity.status(errorCode.getStatus()).body(new ApiResponse<>(errorCode.getCode(), errorCode.getMessage()));
    }
}
