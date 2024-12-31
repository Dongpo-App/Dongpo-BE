package com.dongyang.dongpo.domain.admin.dto;

import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.admin.entity.AdminRole;
import lombok.Data;

@Data
public class SignUpDto {

    private String loginId;
    private String password;
    private String name;


    public Admin toEntity() {
        return Admin.builder()
                .loginId(loginId)
                .name(name)
                .password(password)
                .role(AdminRole.ROLE_GRANT)
                .build();
    }
}
