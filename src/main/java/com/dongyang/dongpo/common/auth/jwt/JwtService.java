package com.dongyang.dongpo.common.auth.jwt;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.auth.dto.JwtToken;
import com.dongyang.dongpo.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private static final String ACCESS_TOKEN_PREFIX = "access_token_";
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token_";
    private static final String BLACKLIST_PREFIX = "blacklist_";
    private static final long ACCESS_TOKEN_VALID_MINUTES = 30;
    private static final long REFRESH_TOKEN_VALID_DAYS = 7;

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
        redisTemplate.opsForValue().set(ACCESS_TOKEN_PREFIX + memberEmail, jwtToken.getAccessToken(), ACCESS_TOKEN_VALID_MINUTES, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + memberEmail, jwtToken.getRefreshToken(), REFRESH_TOKEN_VALID_DAYS, TimeUnit.DAYS);
    }

    public void expireExistingTokens(String memberEmail) {
        String accessTokenKey = ACCESS_TOKEN_PREFIX + memberEmail;
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + memberEmail;

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
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, token, remainingLife, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

    public Claims parseClaims(String token) {
        if (isTokenBlacklisted(token))
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        return jwtUtil.parseClaims(token);
    }
}
