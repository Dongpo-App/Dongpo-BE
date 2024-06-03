package com.dongyang.dongpo.admin.dto;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.domain.AdminRole;
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
