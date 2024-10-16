package com.dongyang.dongpo.domain.auth;

import com.dongyang.dongpo.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppleRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String refreshToken;

    @Builder.Default
    private LocalDateTime updateDate = LocalDateTime.now();

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        this.updateDate = LocalDateTime.now();
    }
}
