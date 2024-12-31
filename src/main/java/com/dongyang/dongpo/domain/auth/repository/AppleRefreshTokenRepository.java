package com.dongyang.dongpo.domain.auth.repository;

import com.dongyang.dongpo.domain.auth.entity.AppleRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleRefreshTokenRepository extends JpaRepository<AppleRefreshToken, Long> {
    Optional<AppleRefreshToken> findBySocialId(String socialId);

    void deleteBySocialId(String socialId);
}
