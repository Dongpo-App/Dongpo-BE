package com.dongyang.dongpo.dto.auth;

import com.dongyang.dongpo.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppleSignupContinueDto {
    private String nickname;
    private String birthday;
    private Member.Gender gender;
    private String socialId;
    private String email;
    private Boolean isServiceTermsAgreed;
    private Boolean isMarketingTermsAgreed;
}
