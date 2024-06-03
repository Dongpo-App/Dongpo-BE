package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }
}
