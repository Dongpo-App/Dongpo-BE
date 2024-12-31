package com.dongyang.dongpo.common.util.auth;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleTokenParser {

    private final ObjectMapper objectMapper;

    public Map<String, String> parseHeader(final String appleToken) {
        try {
            final String encodedHeader = appleToken.split("\\.")[0];
            final String decodedHeader = new String(Base64.getUrlDecoder().decode(encodedHeader), StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonMappingException e) {
            throw new CustomException(ErrorCode.HEADER_PARSING_FAILED);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.HEADER_PARSING_FAILED);
        }
    }

    public Claims extractClaims(String identityToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.CLAIMS_EXTRACTION_FAILED);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.MALFORMED_TOKEN);
        }
    }
}
