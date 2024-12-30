package com.dongyang.dongpo.domain.auth.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
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
    private String nickname;
    private Member.Gender gender;
    private String birthyear;
    private String birthday;
    private String profilePic;
    private Member.SocialType provider;
    private Boolean isServiceTermsAgreed;
    private Boolean isMarketingTermsAgreed;
}
