package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
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


    public ResponseEntity reissueAccessToken(String accessToken) throws Exception {
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(Exception::new);  // 임시

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(Exception::new);  // 임시

        JwtToken jwtToken = jwtTokenProvider.createToken(email, member.getRole());
        refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        return ResponseEntity.ok(jwtToken);
    }
}
