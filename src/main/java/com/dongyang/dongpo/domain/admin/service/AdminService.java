package com.dongyang.dongpo.domain.admin.service;

import com.dongyang.dongpo.domain.admin.dto.ConfrimDto;
import com.dongyang.dongpo.domain.admin.dto.SignUpDto;
import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.admin.entity.AdminRole;
import com.dongyang.dongpo.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignUpDto request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Admin admin = request.toEntity();
        adminRepository.save(admin);
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
