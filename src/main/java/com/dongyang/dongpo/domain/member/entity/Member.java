package com.dongyang.dongpo.domain.member.entity;

import com.dongyang.dongpo.domain.member.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_table")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Member implements UserDetails {
    private static final String LEFT_MEMBER_NICKNAME = "탈퇴한사용자";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String email;

    @Column(length = 32)
    private String nickname;

    @Column(length = 128)
    private String profilePic;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private Title mainTitle;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birthyear;

    private String birthday;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(length = 128)
    private String socialId;

    @CreatedDate
    private LocalDateTime signupDate;

    private LocalDateTime leaveDate;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean isServiceTermsAgreed;

    private Boolean isMarketingTermsAgreed;

    public void updateMemberNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void updateMemberProfilePic(String newProfilePic) {
        this.profilePic = newProfilePic;
    }

    public void updateMemberMainTitle(Title newMainTitle) {
        this.mainTitle = newMainTitle;
    }

    public void setMemberStatusLeave() {
        this.email = null;
        this.nickname = LEFT_MEMBER_NICKNAME + this.id;
        this.profilePic = null;
        this.gender = null;
        this.birthyear = null;
        this.birthday = null;
        this.socialId = null;
        this.leaveDate = LocalDateTime.now();
        this.status = Status.LEAVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
//        return !memberStatus.equals(Status.LEAVE);
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return !memberStatus.equals(Status.INACTIVE);
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
