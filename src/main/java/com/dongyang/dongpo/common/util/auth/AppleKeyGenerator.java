package com.dongyang.dongpo.common.util.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleKeyGenerator {

    @Value("${apple.team.id}")
    private String appleTeamId; // iss

    @Value("${apple.login.key}")
    private String appleLoginKey; // kid

    @Value("${apple.client.id}")
    private String appleClientId; // sub

    @Value("${apple.key.content}")
    private String appleKeyContent; // private key

    private final static String appleAuthUrl = "https://appleid.apple.com"; // aud

    // client_secret 생성
    public String generateClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeaderParam("kid", appleLoginKey)
                .setHeaderParam("alg", "ES256")
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience(appleAuthUrl)
                .setSubject(appleClientId)
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    // key.p8 파일을 읽어와 PrivateKey 객체로 변환
    private PrivateKey getPrivateKey() {
        try {
            Reader pemReader = new StringReader(appleKeyContent.replace("\\n", "\n"));
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();
            return converter.getPrivateKey(object);
        } catch (IOException e) {
            log.error("Error loading Private Key: {}", appleKeyContent);
            throw new RuntimeException("Failed to load private key", e);
        }
    }

}
