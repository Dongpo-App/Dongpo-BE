package com.dongyang.dongpo.domain.auth.dto;

import com.dongyang.dongpo.domain.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AppleSignupContinueDto {
    private String nickname;
    private String birthday;
    private Gender gender;
    private String socialId;
    private String email;
    private Boolean isServiceTermsAgreed;
    private Boolean isMarketingTermsAgreed;
}
