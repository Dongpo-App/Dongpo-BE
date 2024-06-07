package com.dongyang.dongpo.admin.repository;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByLoginId(String loginId);

    List<Admin> findByRole(AdminRole role);
}