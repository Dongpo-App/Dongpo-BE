package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TokenService tokenService;

    private final Member member = mock(Member.class);
    private final RefreshToken refreshToken = mock(RefreshToken.class);

    @Test
    @DisplayName("토큰 재발급")
    void reissueAccessToken() {
        JwtToken mockJwtToken = mock(JwtToken.class);
        when(mockJwtToken.getAccessToken()).thenReturn("AccessToken");
        when(mockJwtToken.getRefreshToken()).thenReturn("RefreshToken");

        Claims claims = mock(Claims.class);
        claims.setSubject("test@email");

        when(jwtTokenProvider.parseClaims(any())).thenReturn(claims);
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(refreshToken));
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createToken(any(), any())).thenReturn(mockJwtToken);

        JwtToken jwtToken = tokenService.reissueAccessToken(any());

        assertNotNull(jwtToken);
        assertNotNull(jwtToken.getAccessToken());
        assertNotNull(jwtToken.getRefreshToken());

        verify(refreshTokenRepository).findByEmail(member.getEmail());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(jwtTokenProvider).createToken(member.getEmail(), member.getRole());
    }


    @Test
    @DisplayName("토큰 재발급 실패 - 토큰 만료")
    void reissueAccessTokenExpired() {
        Claims claims = mock(Claims.class);
        claims.setSubject("test@email");

        when(jwtTokenProvider.parseClaims(any())).thenReturn(claims);

        // Exception
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> tokenService.reissueAccessToken(anyString()));

        assertInstanceOf(CustomException.class, exception);
        assertEquals(ErrorCode.EXPIRED_TOKEN.getMessage(), exception.getMessage());
        verify(refreshTokenRepository).findByEmail(member.getEmail());
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 회원 정보 없음")
    void reissueAccessTokenMemberNotFound() {
        Claims claims = mock(Claims.class);
        claims.setSubject("test@email");

        when(jwtTokenProvider.parseClaims(any())).thenReturn(claims);
        when(refreshTokenRepository.findByEmail(any())).thenReturn(Optional.of(refreshToken));

        // Exception
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> tokenService.reissueAccessToken(anyString()));

        assertInstanceOf(CustomException.class, exception);
        assertEquals(ErrorCode.MEMBER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(memberRepository).findByEmail(any());
    }

    @Test
    @DisplayName("소셜 로그인 - 이미 존재하는 회원일 경우 토큰 발급")
    void social_AlreadyExistMemberTest() {
        JwtToken mockJwtToken = mock(JwtToken.class);

        when(member.getEmail()).thenReturn("test@test.com");
        when(member.getRole()).thenReturn(Member.Role.ROLE_MEMBER);

        when(jwtTokenProvider.createToken(member.getEmail(), member.getRole())).thenReturn(mockJwtToken);
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(refreshToken));

        when(mockJwtToken.getAccessToken()).thenReturn("mockAccessToken");
        when(mockJwtToken.getRefreshToken()).thenReturn("mockRefreshToken");

        JwtToken resultToken = tokenService.social_AlreadyExistMember(member);

        assertNotNull(resultToken);
        assertEquals("mockAccessToken", resultToken.getAccessToken());
        assertEquals("mockRefreshToken", resultToken.getRefreshToken());

        verify(refreshToken).updateRefreshToken(mockJwtToken.getRefreshToken());
        verify(refreshTokenRepository).save(refreshToken);
    }
}