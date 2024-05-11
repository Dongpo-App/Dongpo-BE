package com.dongyang.dongpo.jwt;

import com.dongyang.dongpo.jwt.exception.CustomWorngTokenException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = resolveToken((HttpServletRequest) servletRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
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
            throw new CustomWorngTokenException();

        return token.substring(7);
    }
}
