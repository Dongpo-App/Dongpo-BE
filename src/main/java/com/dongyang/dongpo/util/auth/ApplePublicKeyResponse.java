package com.dongyang.dongpo.util.auth;

import com.dongyang.dongpo.dto.auth.ApplePublicKeyDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
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
