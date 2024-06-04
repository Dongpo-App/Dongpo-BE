package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import com.dongyang.dongpo.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;


    @Transactional
    public JwtToken socialSave(UserInfo userInfo){
        Member member = Member.toEntity(userInfo);

        if (memberRepository.existsByEmail(member.getEmail()))
            return tokenService.alreadyExistMember(member);

        memberRepository.save(member);

        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken refreshToken = RefreshToken.builder()
                .email(member.getEmail())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Registered Member {} success", member.getId());
        return jwtToken;
    }
}
