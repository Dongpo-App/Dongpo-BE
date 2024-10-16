package com.dongyang.dongpo.repository.auth;

import com.dongyang.dongpo.domain.auth.AppleRefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    Optional<AppleRefreshToken> findByMember(Member member);

    void deleteByMember(Member member);
}
