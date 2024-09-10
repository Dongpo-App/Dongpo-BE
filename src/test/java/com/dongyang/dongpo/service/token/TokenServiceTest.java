package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
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

    private Member member = mock(Member.class);
    private RefreshToken refreshToken = mock(RefreshToken.class);
    private String token = "testToken";

    @Test
    @DisplayName("토큰_재발급")
    void reissueAccessToken() throws Exception {
        Claims claims = mock(Claims.class);
        JwtToken mockJwtToken = mock(JwtToken.class);

        when(mockJwtToken.getAccessToken()).thenReturn("testAccessToken");
        when(mockJwtToken.getRefreshToken()).thenReturn("testRefreshToken");

        when(member.getEmail()).thenReturn("test@test.com");
        when(member.getRole()).thenReturn(Member.Role.ROLE_MEMBER);

        when(jwtTokenProvider.parseClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("test@test.com");
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));
        when(refreshTokenRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(refreshToken));
        when(jwtTokenProvider.createToken(member.getEmail(), member.getRole())).thenReturn(mockJwtToken);

        JwtToken jwtToken = tokenService.reissueAccessToken(token);

        assertNotNull(jwtToken);
        assertNotNull(jwtToken.getAccessToken());
        assertNotNull(jwtToken.getRefreshToken());

        verify(memberRepository).findByEmail(member.getEmail());
        verify(refreshTokenRepository).findByEmail(member.getEmail());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(jwtTokenProvider).createToken(member.getEmail(), member.getRole());
    }

    @Test
    @DisplayName("토큰_재발급_실패")
    void reissueAccessTokenFail() {
    }

    @Test
    @DisplayName("소셜로그인_토큰발급")
    void alreadyExistMember() {
    }
}