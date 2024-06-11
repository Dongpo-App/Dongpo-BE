package com.dongyang.dongpo.dto.auth;

import com.dongyang.dongpo.domain.member.Member;
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
    private String name;
    private String nickname;
    private Member.Gender gender;
    private String age;
    private String profilePic;
    private Member.SocialType provider;
}
