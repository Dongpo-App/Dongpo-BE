package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.auth.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.JwtTokenReissueDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.auth.RefreshTokenRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public JwtToken reissueAccessToken(JwtTokenReissueDto jwtTokenReissueDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(jwtTokenReissueDto.getRefreshToken())
                .orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_TOKEN));

        String email = jwtTokenProvider.parseClaims(jwtTokenReissueDto.getRefreshToken()).getSubject();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        refreshToken.updateRefreshToken(jwtToken.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        log.info("Refresh Token Reissued : {}", member.getEmail());
        return jwtToken;
    }

    @Transactional
    public JwtToken createTokenForLoginMember(Member member){
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
        log.info("Member Login : {}", member.getEmail());
        return jwtToken;
    }

    @Transactional
    public void expireTokens(String email, String authorization) {
        refreshTokenRepository.deleteById(email);

        jwtTokenProvider.blacklistToken(authorization);
    }
}
