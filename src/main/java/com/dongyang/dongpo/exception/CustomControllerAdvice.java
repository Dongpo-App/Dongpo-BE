package com.dongyang.dongpo.exception;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.jwt.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {
    private static final ApiResponse<String> response = new ApiResponse<>();

    @ExceptionHandler(CustomUnsupportedException.class)
    public ResponseEntity<ApiResponse<String>> handleSignatureException() {
        response.setMessage("지원하지않는 형식입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomMalformedException.class)
    public ResponseEntity<ApiResponse<String>> handleMalformedJwtException() {
        response.setMessage("올바르지 않은 토큰입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomExpiredException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException() {
        response.setMessage("토큰이 만료되었습니다. 다시 로그인해주세요.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomWorngTokenException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException() {
        response.setMessage("잘못된 토큰입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomClaimsException.class)
    public ResponseEntity<ApiResponse<String>> handleClaimJwtException() {
        response.setMessage("Claim 검증 실패");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // -----------------------------

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> customException(CustomException e) {
        response.setMessage(e.getMessage());
        HttpStatus status = HttpStatus.valueOf(e.getErrorCode().getCode());

        return ResponseEntity.status(status).body(response);
    }



}
