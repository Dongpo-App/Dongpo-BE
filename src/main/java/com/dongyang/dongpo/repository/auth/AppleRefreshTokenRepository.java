package com.dongyang.dongpo.repository.auth;

import com.dongyang.dongpo.domain.auth.AppleRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    Optional<AppleRefreshToken> findBySocialId(String socialId);

    void deleteBySocialId(String socialId);
}
