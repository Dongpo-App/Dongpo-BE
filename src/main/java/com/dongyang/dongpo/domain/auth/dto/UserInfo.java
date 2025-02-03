package com.dongyang.dongpo.domain.auth.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 정보")
public class UserInfo {

    private static final Title DEFAULT_MAIN_TITLE = Title.BASIC_TITLE;
    private static final Role DEFAULT_ROLE = Role.ROLE_MEMBER;
    private static final Status DEFAULT_STATUS = Status.ACTIVE;

    @Schema(description = "소셜 ID", example = "1234567890")
    @NotNull
    private String socialId;

    @Schema(description = "이메일", example = "test@example.com")
    @NotNull
    private String email;

    @Schema(description = "닉네임", example = "test")
    @NotNull
    private String nickname;

    @Schema(description = "성별", example = "GEN_MALE")
    @NotNull
    private Gender gender;

    @Schema(description = "생년", example = "1990")
    @NotNull
    private String birthyear;

    @Schema(description = "생일", example = "01-01")
    @NotNull
    private String birthday;

    @Schema(description = "프로필 사진", example = "https://example.com/profile.jpg")
    @Null
    private String profilePic;

    @Schema(description = "소셜 타입", example = "KAKAO")
    @NotNull
    private SocialType provider;

    @Schema(description = "서비스 약관 동의 여부", example = "true")
    @NotNull
    private Boolean isServiceTermsAgreed;

    @Schema(description = "마케팅 약관 동의 여부", example = "true")
    @NotNull
    private Boolean isMarketingTermsAgreed;

    public Member toMemberEntity() {
        return Member.builder()
                .email(this.email)
                .nickname(this.nickname)
                .birthyear(this.birthyear)
                .birthday(this.birthday)
                .gender(this.gender)
                .socialId(this.socialId)
                .socialType(this.provider)
                .role(DEFAULT_ROLE)
                .profilePic(this.profilePic)
                .mainTitle(DEFAULT_MAIN_TITLE)
                .status(DEFAULT_STATUS)
                .isServiceTermsAgreed(this.isServiceTermsAgreed)
                .isMarketingTermsAgreed(this.isMarketingTermsAgreed)
                .build();
    }
}
