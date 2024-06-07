package com.dongyang.dongpo.admin;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.domain.admin.AdminRole;
import com.dongyang.dongpo.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 테스트용 ADMIN 계정 생성
     * SUPER_ADMIN -
     *      ID : super
     *      PW : super
     * ADMIN - ID :
     *      admin
     *      PW : admin
     */
    @Override
    public void run(String... args) throws Exception {
        Admin superAdmin = Admin.builder()
                .name("superAdmin")
                .loginId("super")
                .password(passwordEncoder.encode("super"))
                .role(AdminRole.ROLE_SUPER_ADMIN)
                .build();

        Admin admin = Admin.builder()
                .name("admin")
                .loginId("admin")
                .password(passwordEncoder.encode("admin"))
                .role(AdminRole.ROLE_ADMIN)
                .build();

        adminRepository.save(admin);
        adminRepository.save(superAdmin);
    }
}
