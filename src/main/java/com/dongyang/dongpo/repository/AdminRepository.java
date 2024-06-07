package com.dongyang.dongpo.repository;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.domain.admin.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByLoginId(String loginId);

    List<Admin> findByRole(AdminRole role);
}
