package com.dongyang.dongpo.dto.admin;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.domain.admin.AdminRole;
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
