package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public ResponseEntity<?> socialSave(UserInfo userInfo){
        Member member = Member.toEntity(userInfo);
        memberRepository.save(member);

        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken refreshToken = RefreshToken.builder()
                .email(member.getEmail())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return ResponseEntity.ok(jwtToken);
    }

    
}
