package com.dongyang.dongpo.exception;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.exception.data.DataNotFoundException;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
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

    /**
     * ------------- 401 -------------
     */

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberNotFoundException() {
        response.setMessage("회원을 찾지 못하였습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleStoreNotFoundException() {
        response.setMessage("점포를 찾지 못하였습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * ------------- 404 -------------
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleDataNotFoundException() {
        response.setMessage("데이터를 찾지 못하였습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
