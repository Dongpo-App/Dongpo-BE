package com.dongyang.dongpo.domain.member;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String nickname;
    private Gender gender;
    private int SocialId;
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime signup_date;

    @ColumnDefault("null")
    private LocalDateTime leave_date;

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
    public boolean isAccountNonExpired() { // 계정 만료
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // 계정 잠금
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // 자격증명 만료
        return true;
    }

    @Override
    public boolean isEnabled() { // 계정 활성화
        return true;
    }
}
