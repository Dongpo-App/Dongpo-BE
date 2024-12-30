package com.dongyang.dongpo.common.util.auth;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.auth.dto.ApplePublicKeyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ApplePublicKeyResponse {

    private List<ApplePublicKeyDto> keys;

    public ApplePublicKeyResponse(List<ApplePublicKeyDto> keys) {
        this.keys = List.copyOf(keys);
    }

    public ApplePublicKeyDto getMatchingKey(final String alg, final String kid) {
        return keys.stream()
                .filter(key -> key.isSameAlg(alg) && key.isSameKid(kid))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.SOCIAL_TOKEN_NOT_VALID));
    }

}
