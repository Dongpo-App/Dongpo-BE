package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MyPageDto getMyPageIndex(String accessToken) throws Exception {
        return MyPageDto.toEntity(memberRepository.findByEmail(jwtTokenProvider.parseClaims(accessToken).getSubject()).orElseThrow(MemberNotFoundException::new));
    }
}
