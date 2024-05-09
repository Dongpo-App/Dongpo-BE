package com.dongyang.dongpo.exception;

import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

@RestControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(CustomUnsupportedException.class)
    public ResponseEntity handleSignatureException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("지원하지않는 형식입니다.");
    }

    @ExceptionHandler(CustomMalformedException.class)
    public ResponseEntity handleMalformedJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("올바르지 않은 토큰입니다.");
    }

    @ExceptionHandler(CustomExpiredException.class)
    public ResponseEntity handleExpiredJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 만료되었습니다. 다시 로그인해주세요.");
    }

    @ExceptionHandler(CustomWorngTokenException.class)
    public ResponseEntity handleIllegalArgumentException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 토큰입니다.");
    }

    @ExceptionHandler(CustomClaimsException.class)
    public ResponseEntity handleClaimJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Claim 검증 실패");
    }
    /**
     *  ------------- 401 -------------
     */

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity handleMemberNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾지 못하였습니다.");
    }
}
