package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;

    public JwtToken createTokenForLoginMember(Member member) {
        JwtToken jwtToken = jwtUtil.createToken(member.getEmail(), member.getRole());
        expireExistingTokens(member.getEmail());
        saveTokens(member.getEmail(), jwtToken);

        log.info("Member Login : {}", member.getEmail());
        return jwtToken;
    }

    public JwtToken reissueAccessToken(Member member) {
        JwtToken jwtToken = jwtUtil.createToken(member.getEmail(), member.getRole());
        expireExistingTokens(member.getEmail());
        saveTokens(member.getEmail(), jwtToken);

        log.info("Refresh Token Reissued : {}", member.getEmail());
        return jwtToken;
    }

    private void saveTokens(String memberEmail, JwtToken jwtToken) {
        redisTemplate.opsForValue().set("access_token_" + memberEmail, jwtToken.getAccessToken(), 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("refresh_token_" + memberEmail, jwtToken.getRefreshToken(), 7, TimeUnit.DAYS);
    }

    public void expireExistingTokens(String memberEmail) {
        String accessTokenKey = "access_token_" + memberEmail;
        String refreshTokenKey = "refresh_token_" + memberEmail;

        if (redisTemplate.hasKey(accessTokenKey)) {
            String accessToken = redisTemplate.opsForValue().get(accessTokenKey);
            blacklistToken(accessToken);
            redisTemplate.delete(accessTokenKey);
        }

        if (redisTemplate.hasKey(refreshTokenKey)) {
            String refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
            blacklistToken(refreshToken);
            redisTemplate.delete(refreshTokenKey);
        }
    }

    private void blacklistToken(String token) {
        long remainingLife = jwtUtil.getRemainingTokenLife(token);
        redisTemplate.opsForValue().set("blacklist_" + token, token, remainingLife, TimeUnit.MILLISECONDS);
    }

    public void validateToken(String token) {
        if (isTokenBlacklisted(token))
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        jwtUtil.validateToken(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist_" + token);
    }

    public Claims parseClaims(String token) {
        return jwtUtil.parseClaims(token);
    }
}
