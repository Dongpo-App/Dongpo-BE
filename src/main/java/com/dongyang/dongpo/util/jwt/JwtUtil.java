package com.dongyang.dongpo.util.jwt;

import com.dongyang.dongpo.domain.member.Member.Role;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long ACCESSTOKEN_VALIDTIME = 30 * 60 * 1000L; // 30분
    private final long REFRESHTOKEN_VALIDTIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    private final String GRANT_TYPE = "Bearer";
    private final String ROLE = "role";

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public JwtToken createToken(String email, Role role) {
        Date now = new Date();
        Date accessTokenExpiredTime = new Date(now.getTime() + ACCESSTOKEN_VALIDTIME);
        Date refreshTokenExpiredTime = new Date(now.getTime() + REFRESHTOKEN_VALIDTIME);
        Claims claims = createClaims(email, role);

        String accessToken = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType(GRANT_TYPE)
                .claims(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return header;
    }

    private Claims createClaims(String email, Role role) {
        Claims claims = Jwts.claims().setIssuer(email);
        claims.put(ROLE, role);
        return claims;
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            throw new CustomException(ErrorCode.MALFORMED_TOKEN); // jwt서명이 유효하지 않음
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN); // 지원하지않는 jwt 토큰
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN); // 토큰 시간 만료
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND); // claims 없음
        } catch (ClaimJwtException e) {
            throw new CustomException(ErrorCode.CLAIMS_NOT_VALID); // Claim 검증 실패
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }

    // 토큰의 남은 유효기간을 반환
    public long getRemainingTokenLife(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }
}
