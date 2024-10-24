package com.dongyang.dongpo.repository.auth;

import com.dongyang.dongpo.domain.auth.TokenBlacklist;
import org.springframework.data.repository.CrudRepository;

public interface TokenBlacklistRepository extends CrudRepository<TokenBlacklist, String> {
    boolean existsByAccessToken(String accessToken);
}
