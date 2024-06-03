package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.domain.AdminRole;
import com.dongyang.dongpo.admin.repository.AdminRepository;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public List<Admin> findProcessAdmin(){
        return adminRepository.findByRole(AdminRole.ROLE_GRANT);
    }
}
