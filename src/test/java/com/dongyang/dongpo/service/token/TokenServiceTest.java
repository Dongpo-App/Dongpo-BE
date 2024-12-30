package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.util.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private Member member;

    @Mock
    private JwtToken jwtToken;

    @Test
    @DisplayName("토큰 재발급")
    void reissueAccessToken() {
        // Given
        final String email = "test@example.com";
        final String accessTokenKey = "access_token_" + email;
        final String refreshTokenKey = "refresh_token_" + email;
        final Member.Role role = Member.Role.ROLE_MEMBER;

        when(member.getEmail()).thenReturn(email);
        when(member.getRole()).thenReturn(role);
        when(jwtUtil.createToken(email, role)).thenReturn(jwtToken);
        when(jwtToken.getAccessToken()).thenReturn("newAccessToken");
        when(jwtToken.getRefreshToken()).thenReturn("newRefreshToken");

        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        JwtToken result = tokenService.reissueAccessToken(member);

        // Then
        assertEquals(jwtToken, result);
        verify(redisTemplate).hasKey(accessTokenKey);
        verify(redisTemplate).hasKey(refreshTokenKey);
        verify(valueOperations).set(eq(accessTokenKey), eq("newAccessToken"), eq(30L), eq(TimeUnit.MINUTES));
        verify(valueOperations).set(eq(refreshTokenKey), eq("newRefreshToken"), eq(7L), eq(TimeUnit.DAYS));
    }


    @Test
    @DisplayName("토큰 재발급 실패 - 토큰 만료")
    void reissueAccessTokenExpired() {
        // Given
        final String email = "test@example.com";
        final Member.Role role = Member.Role.ROLE_MEMBER;

        when(member.getEmail()).thenReturn(email);
        when(member.getRole()).thenReturn(role);
        when(jwtUtil.createToken(email, role)).thenThrow(new CustomException(ErrorCode.EXPIRED_TOKEN));

        // When & Then
        assertThrows(CustomException.class, () -> tokenService.reissueAccessToken(member));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 회원 정보 없음")
    void reissueAccessTokenMemberNotFound() {
        // Given
        final String email = "nonexistent@example.com";
        final Member.Role role = Member.Role.ROLE_MEMBER;

        when(member.getEmail()).thenReturn(email);
        when(member.getRole()).thenReturn(role);
        when(jwtUtil.createToken(email, role)).thenThrow(new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // When & Then
        assertThrows(CustomException.class, () -> tokenService.reissueAccessToken(member));
    }

    @Test
    @DisplayName("소셜 로그인 - 이미 존재하는 회원일 경우 토큰 발급")
    void createTokenForLoginMember() {
        // Given
        final String email = "test@example.com";
        final String accessTokenKey = "access_token_" + email;
        final String refreshTokenKey = "refresh_token_" + email;
        final Member.Role role = Member.Role.ROLE_MEMBER;

        when(member.getEmail()).thenReturn(email);
        when(member.getRole()).thenReturn(role);
        when(jwtUtil.createToken(email, role)).thenReturn(jwtToken);

        when(redisTemplate.hasKey(accessTokenKey)).thenReturn(true);
        when(redisTemplate.hasKey(refreshTokenKey)).thenReturn(true);
        when(jwtToken.getAccessToken()).thenReturn("accessToken");
        when(jwtToken.getRefreshToken()).thenReturn("refreshToken");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // When
        JwtToken result = tokenService.createTokenForLoginMember(member);

        // Then
        assertEquals(jwtToken, result);
        verify(redisTemplate).hasKey(accessTokenKey);
        verify(redisTemplate).hasKey(refreshTokenKey);
        verify(redisTemplate).delete(accessTokenKey);
        verify(redisTemplate).delete(refreshTokenKey);
        verify(valueOperations).set(eq(accessTokenKey), eq("accessToken"), eq(30L), eq(TimeUnit.MINUTES));
        verify(valueOperations).set(eq(refreshTokenKey), eq("refreshToken"), eq(7L), eq(TimeUnit.DAYS));
    }

    @Test
    @DisplayName("기존 토큰 만료")
    void expireExistingTokens() {
        // Given
        final String email = "test@example.com";
        final String accessTokenKey = "access_token_" + email;
        final String refreshTokenKey = "refresh_token_" + email;

        when(redisTemplate.hasKey(accessTokenKey)).thenReturn(true);
        when(redisTemplate.hasKey(refreshTokenKey)).thenReturn(true);

        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(accessTokenKey)).thenReturn("oldAccessToken");
        when(valueOperations.get(refreshTokenKey)).thenReturn("oldRefreshToken");
        when(jwtUtil.getRemainingTokenLife(anyString())).thenReturn(1000L);

        // When
        tokenService.expireExistingTokens(email);

        // Then
        verify(redisTemplate).delete(accessTokenKey);
        verify(redisTemplate).delete(refreshTokenKey);
        verify(valueOperations).set(eq("blacklist_oldAccessToken"), eq("oldAccessToken"), eq(1000L), eq(TimeUnit.MILLISECONDS));
        verify(valueOperations).set(eq("blacklist_oldRefreshToken"), eq("oldRefreshToken"), eq(1000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("토큰 검증 - 토큰이 블랙리스트에 있을 경우")
    void validateTokenBlacklisted() {
        // Given
        final String token = "blacklistedToken";
        when(redisTemplate.hasKey("blacklist_" + token)).thenReturn(true);

        // When & Then
        assertThrows(CustomException.class, () -> tokenService.validateToken(token));
    }

    @Test
    @DisplayName("토큰 검증 - 토큰이 블랙리스트에 없을 경우")
    void validateTokenNotBlacklisted() {
        // Given
        final String token = "validToken";
        when(redisTemplate.hasKey("blacklist_" + token)).thenReturn(false);

        // When
        tokenService.validateToken(token);

        // Then
        verify(jwtUtil).validateToken(token);
    }
}
