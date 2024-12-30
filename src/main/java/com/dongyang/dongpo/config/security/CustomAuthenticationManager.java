package com.dongyang.dongpo.config.security;

import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.util.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (jwtAuthenticationProvider.supports(authentication.getClass()))
            return jwtAuthenticationProvider.authenticate(authentication);

        throw new CustomException(ErrorCode.UNSUPPORTED_AUTHENTICATION_TYPE);
    }
}
