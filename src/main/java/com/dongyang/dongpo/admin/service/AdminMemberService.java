package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.domain.AdminRole;
import com.dongyang.dongpo.admin.dto.ConfrimDto;
import com.dongyang.dongpo.admin.repository.AdminRepository;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public List<Admin> findProcessAdmin(){
        return adminRepository.findByRole(AdminRole.ROLE_GRANT);
    }


    @Transactional
    public void approveAdmin(ConfrimDto confrimDto) {
        for (Long id : confrimDto.getSelectedGrants()) {
            Admin admin = adminRepository.findById(id).get();
            admin.confirm(AdminRole.ROLE_ADMIN);
            adminRepository.save(admin);

            log.info("Approve Admin ID : {}", id);
        }
    }

    @Transactional
    public void rejectAdmin(ConfrimDto confrimDto) {
        for (Long id : confrimDto.getSelectedGrants()) {
            Admin admin = adminRepository.findById(id).get();
            admin.confirm(AdminRole.ROLE_REJECT);
            adminRepository.save(admin);

            log.info("Reject Admin ID : {}", id);
        }
    }
}
