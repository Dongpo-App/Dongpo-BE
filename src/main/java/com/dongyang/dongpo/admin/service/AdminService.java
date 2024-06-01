package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.SignUpDto;
import com.dongyang.dongpo.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignUpDto request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Admin admin = request.toEntity();
        adminRepository.save(admin);
    }

}
