package com.dongyang.dongpo.repository;

import com.dongyang.dongpo.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
