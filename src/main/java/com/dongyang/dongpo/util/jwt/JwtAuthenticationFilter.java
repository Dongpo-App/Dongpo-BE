package com.dongyang.dongpo.util.jwt;

import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = resolveToken((HttpServletRequest) servletRequest);

            jwtTokenProvider.isBlacklisted(token);

            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e){
            servletRequest.setAttribute("exception", e);
        }finally {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String resolveToken(HttpServletRequest servletRequest) throws Exception{
        String token = servletRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer "))
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND);

        return token.substring(7);
    }
}
