package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.dto.auth.UserInfo;
import jakarta.persistence.*;
import lombok.*;
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
@Table(name="member_table")
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 64)
    private String email;

    @Column(length = 32)
    private String name;

    @Column(length = 32)
    private String nickname;

    @Column(length = 128)
    private String profilePic;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String ageGroup;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(length = 128)
    private String socialId;

    @Builder.Default
    private LocalDateTime signupDate = LocalDateTime.now();

    private LocalDateTime leaveDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static Member toEntity(UserInfo userInfo){
        return Member.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .nickname(userInfo.getNickname())
                .ageGroup(userInfo.getAge())
                .gender(userInfo.getGender())
                .socialId(userInfo.getId())
                .socialType(userInfo.getProvider())
                .role(Role.ROLE_MEMBER)
                .profilePic(userInfo.getProfilePic())
                .status(Member.Status.ACTIVE)
                .build();
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

    public enum Role {
        ROLE_MEMBER, ROLE_ADMIN
    }

    public enum Gender {
        GEN_MALE, NONE, GEN_FEMALE
    }

    public enum SocialType {
        KAKAO, APPLE, NAVER
    }

    public enum Status {
        ACTIVE, INACTIVE, LEAVE
    }
}
