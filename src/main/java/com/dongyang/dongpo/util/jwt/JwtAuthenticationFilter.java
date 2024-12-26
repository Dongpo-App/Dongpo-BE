package com.dongyang.dongpo.util.jwt;

import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String HEADER_PREFIX = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request.getHeader(HEADER_PREFIX));
            validateToken(token);
            setAuthentication(getAuthentication(token));
        } catch (Exception e) {
            request.setAttribute("exception", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    // 토큰 유효성 검증 및 파싱
    private String resolveToken(String authorizationHeader) {
        if (StringUtil.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(TOKEN_PREFIX))
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND);

        return jwtUtil.parseAccessToken(authorizationHeader);
    }

    // 토큰 유효성 검증
    private void validateToken(String token) {
        jwtUtil.validateToken(token);
    }

    // 토큰으로 사용자 인증 객체 호출
    private Authentication getAuthentication(String token) {
        return jwtUtil.getAuthentication(token);
    }

    // SecurityContextHolder 사용자 인증 객체 저장
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
