package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.service.oauth2.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

}
