package com.dongyang.dongpo.service.oauth2.handler;

import com.dongyang.dongpo.domain.member.Role;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");

        boolean isExists = oAuth2User.getAttribute("exists");
        Role role = Role.valueOf(oAuth2User.getAuthorities().stream()
                .findFirst()
                        .orElseThrow(IllegalAccessError::new)
                                .getAuthority());

        if (isExists){
            JwtToken jwtToken = jwtTokenProvider.createToken(email, role);

        }
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
