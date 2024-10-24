package com.dongyang.dongpo.domain.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@RedisHash(value = "blacklist", timeToLive = 1800) // 30분
public class TokenBlacklist {
    @Id
    @Indexed
    private String accessToken;
}
