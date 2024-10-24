package com.dongyang.dongpo.dto.admin.member;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String profilePic;
    private Title mainTitle;
    private List<MemberTitle> memberTitles;
    private Member.Role role;
    private Member.Gender gender;
    private String ageGroup;
    private Member.SocialType socialType;
    private String socialId;
    private LocalDateTime signupDate;
    private LocalDateTime leaveDate;
    private Member.Status status;

}
