package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.jwt.exception.CustomExpiredException;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;


    public ResponseEntity reissueAccessToken(String token) throws Exception {
        String email = jwtTokenProvider.parseClaims(token).getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(CustomExpiredException::new);

        JwtToken jwtToken = jwtTokenProvider.createToken(email, member.getRole());
        refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return ResponseEntity.ok(jwtToken);
    }
}
