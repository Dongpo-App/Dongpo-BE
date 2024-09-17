package com.dongyang.dongpo.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization 헤더를 찾아 검증한다
        String authorization = request.getHeader("Authorization");

        // 인증 정보가 아예 없을 경우 바로 다음 필터로 넘어간다
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken 파싱
        String accessToken = jwtTokenProvider.parseAccessToken(authorization);
        try {
            if (jwtTokenProvider.validateToken(accessToken))
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(accessToken));
        } catch (Exception e) {
            log.error("Error while parsing JWT Token: ", e);
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }
}
