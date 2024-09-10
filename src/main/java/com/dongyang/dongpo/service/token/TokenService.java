package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.jwt.exception.CustomExpiredException;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public JwtToken reissueAccessToken(String token) throws Exception {
        String email = jwtTokenProvider.parseClaims(token).getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(CustomExpiredException::new);

        JwtToken jwtToken = jwtTokenProvider.createToken(email, member.getRole());
        refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        log.info("Refresh Token Reissued : {}", member.getId());
        return jwtToken;
    }

    @Transactional
    public JwtToken social_AlreadyExistMember(Member member){
        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail()).orElse(null);

        if (refreshToken != null)
            refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        else
            refreshToken = RefreshToken.builder()
                    .refreshToken(jwtToken.getRefreshToken())
                    .email(member.getEmail())
                    .build();

        refreshTokenRepository.save(refreshToken);
        log.info("(Login) Refresh Token Reissued : {}", member.getId());
        return jwtToken;
    }
}
