package com.dongyang.dongpo.admin.dto;

import com.dongyang.dongpo.admin.domain.Admin;
import lombok.Data;

@Data
public class SignUpDto {

    private String loginId;
    private String password;


    public Admin toEntity() {
        return Admin.builder()
                .loginId(loginId)
                .password(password)
                .role("ROLE_ADMIN")
                .build();
    }
}
