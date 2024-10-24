package com.dongyang.dongpo.repository.auth;

import com.dongyang.dongpo.domain.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByEmail(String email);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteById(String email);
}
