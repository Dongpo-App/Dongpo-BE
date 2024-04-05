package com.dongyang.dongpo.dto.auth;

import com.dongyang.dongpo.domain.member.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private String id;
    private String email;
    private SocialType provider;
}
