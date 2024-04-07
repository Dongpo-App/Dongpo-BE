package com.dongyang.dongpo.domain.member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="member_table")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(unique = true)
    private String memberEmail;

    // @Column(nullable = false)
    private String memberName;

    // @Column(nullable = false)
    private String memberNickname;

    private String memberProfilePic;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private Role memberRole;

    @Enumerated(EnumType.STRING)
    private Gender memberGender;

    private Integer memberAgeGroup;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private SocialType socialType;

    // @Column(nullable = false)
    private String socialId;

    // @Column(nullable = false)
    private LocalDateTime signupDate;

    private LocalDateTime leaveDate;

    @Enumerated(EnumType.STRING)
    // @Column(nullable = false)
    private Status memberStatus;

    public enum Role {
        ROLE_MEMBER, ROLE_ADMIN
    }

    public enum Gender {
        GEN_MALE, GEN_FEMALE
    }

    public enum SocialType {
        KAKAO, APPLE, NAVER
    }

    public enum Status {
        ACTIVE, INACTIVE, LEAVE
    }
}
