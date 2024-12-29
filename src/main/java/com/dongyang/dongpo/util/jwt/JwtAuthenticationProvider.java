package com.dongyang.dongpo.util.jwt;

import com.dongyang.dongpo.config.security.CustomUserDetailsService;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.service.token.TokenService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String ROLE = "role";

    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        tokenService.validateToken(token);
        return getAuthentication(token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = tokenService.parseClaims(accessToken);

        if (claims == null || claims.get(ROLE) == null)
            throw new CustomException(ErrorCode.CLAIMS_NOT_FOUND);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLE).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getIssuer());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
