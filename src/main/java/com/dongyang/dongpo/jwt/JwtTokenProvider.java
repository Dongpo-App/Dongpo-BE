package com.dongyang.dongpo.jwt;

import com.dongyang.dongpo.domain.member.Member.Role;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.jwt.exception.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final Key key;
    private final long ACCESSTOKEN_VALIDTIME = 1800000; // 30분
    private final long REFRESHTOKEN_VALIDTIME = 604800000; // 7일

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
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
            throw new CustomMalformedException(); // jwt서명이 유효하지 않음
        } catch (UnsupportedJwtException e){
            log.error(e.getMessage());
            throw new CustomUnsupportedException();  // 지원하지않는 jwt 토큰
        } catch (ExpiredJwtException e){
            log.error(e.getMessage());
            throw new CustomExpiredException();  // 토큰 시간 만료
        } catch (IllegalArgumentException e){
            log.error(e.getMessage());
            throw new CustomWorngTokenException(); // claims 없음
        } catch (ClaimJwtException e) {
            log.error(e.getMessage());
            throw new CustomClaimsException(); // Claim 검증 실패
        }
    }

    public Authentication getAuthentication(String accessToken) throws Exception {
        Claims claims = parseClaims(accessToken);

        if (claims == null || claims.get("role") == null)
            throw new CustomClaimsException();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseClaims(String accessToken){
        String token = accessToken.substring(7);

        return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
}
