package com.dongyang.dongpo.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "refreshToken", timeToLive = 604800000) // 7일
public class RefreshToken {

    @Id
    private Long id;

    private String email;
    private String refreshToken;
}
