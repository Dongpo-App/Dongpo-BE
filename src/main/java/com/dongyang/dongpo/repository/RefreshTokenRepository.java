package com.dongyang.dongpo.repository;

import com.dongyang.dongpo.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByEmail(String email);
}
