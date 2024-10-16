package com.dongyang.dongpo.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplePublicKeyDto {
    private final String kty;
    private final String kid;
    private final String use;
    private final String alg;
    private final String n;
    private final String e;

    public boolean isSameAlg(final String alg) {
        return this.alg.equals(alg);
    }

    public boolean isSameKid(final String kid) {
        return this.kid.equals(kid);
    }

}
