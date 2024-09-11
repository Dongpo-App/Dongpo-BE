package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.jwt.exception.CustomExpiredException;
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

    private Member member = mock(Member.class);
    private RefreshToken refreshToken = mock(RefreshToken.class);
    private String token = "testToken";

    @Test
    @DisplayName("토큰 재발급")
    void reissueAccessToken() throws Exception {
        JwtToken mockJwtToken = mock(JwtToken.class);
        when(mockJwtToken.getAccessToken()).thenReturn("testAccessToken");
        when(mockJwtToken.getRefreshToken()).thenReturn("testRefreshToken");

        when(member.getEmail()).thenReturn("test@test.com");
        when(member.getRole()).thenReturn(Member.Role.ROLE_MEMBER);

        Claims claims = mock(Claims.class);
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@test.com");
        when(jwtTokenProvider.createToken(member.getEmail(), member.getRole())).thenReturn(mockJwtToken);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(refreshToken));

        JwtToken jwtToken = tokenService.reissueAccessToken(member);

        assertNotNull(jwtToken);
        assertNotNull(jwtToken.getAccessToken());
        assertNotNull(jwtToken.getRefreshToken());

        verify(memberRepository).findByEmail(member.getEmail());
        verify(refreshTokenRepository).findByEmail(member.getEmail());

        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(jwtTokenProvider).createToken(member.getEmail(), member.getRole());
    }


    @Test
    @DisplayName("토큰 재발급 실패 - 토큰 만료")
    void reissueAccessTokenFail() {
        Claims claims = mock(Claims.class);
        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@test.com");

        when(member.getEmail()).thenReturn("test@test.com");
        when(member.getRole()).thenReturn(Member.Role.ROLE_MEMBER);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // Exception
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            tokenService.reissueAccessToken(member);
        });

        assertTrue(exception instanceof CustomExpiredException);
        verify(memberRepository).findByEmail(member.getEmail());
        verify(refreshTokenRepository).findByEmail(member.getEmail());
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