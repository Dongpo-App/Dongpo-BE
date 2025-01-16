package com.dongyang.dongpo.common.auth;

import com.dongyang.dongpo.common.auth.jwt.JwtService;
import com.dongyang.dongpo.common.auth.userdetails.CustomUserDetailsService;
import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
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
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final String ROLE = "role";

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        return getAuthentication(token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = jwtService.parseClaims(accessToken);

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
