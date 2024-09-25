package com.dongyang.dongpo.jwt;

import com.dongyang.dongpo.config.security.CustomUserDetailsService;
import com.dongyang.dongpo.domain.member.Member.Role;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long ACCESSTOKEN_VALIDTIME = 30 * 60 * 1000L; // 30분
    private final long REFRESHTOKEN_VALIDTIME = 7L * 24 * 60 * 60 * 1000; // 7일

    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService CustomUserDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.customUserDetailsService = CustomUserDetailsService;
    }

    public JwtToken createToken(String email, Role role){
        Date now = new Date();
        Date accessTokenExpiredTime = new Date(now.getTime() + ACCESSTOKEN_VALIDTIME);
        Date refreshTokenExpiredTime = new Date(now.getTime() + REFRESHTOKEN_VALIDTIME);
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(refreshTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .claims(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e){
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.MALFORMED_TOKEN); // jwt서명이 유효하지 않음
        } catch (UnsupportedJwtException e){
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.UNSUPPORTED_TOKEN); // 지원하지않는 jwt 토큰
        } catch (ExpiredJwtException e){
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.EXPIRED_TOKEN); // 토큰 시간 만료
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND); // claims 없음
        } catch (ClaimJwtException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.CLAIMS_NOT_VALID); // Claim 검증 실패
        }
    }

    public Authentication getAuthentication(String accessToken) throws Exception {
        Claims claims = parseClaims(accessToken);

        if (claims == null || claims.get("role") == null)
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (Exception e) {
            log.error("Error parsing claims: ", e);
            throw new RuntimeException(e);
        }
    }

    public String parseAccessToken(String authorization) {
        return authorization.substring(7);
    }
}
