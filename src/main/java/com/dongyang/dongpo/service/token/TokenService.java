package com.dongyang.dongpo.service.token;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
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

    @Transactional
    public JwtToken reissueAccessToken(Member member) {
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_TOKEN));

        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
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
