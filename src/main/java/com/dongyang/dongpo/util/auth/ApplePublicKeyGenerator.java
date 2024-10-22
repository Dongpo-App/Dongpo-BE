package com.dongyang.dongpo.util.auth;

import com.dongyang.dongpo.dto.auth.ApplePublicKeyDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {

    public PublicKey generate(Map<String, String> headers, ApplePublicKeyResponse publicKeys) {
        ApplePublicKeyDto applePublicKey = publicKeys.getMatchingKey(
                headers.get("alg"),
                headers.get("kid")
        );
        return generatePublicKey(applePublicKey);
    }

    private PublicKey generatePublicKey(ApplePublicKeyDto applePublicKey) {
        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new CustomException(ErrorCode.APPLE_PUBLIC_KEY_GENERATION_FAILED);
        }
    }


}
