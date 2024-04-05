package com.dongyang.dongpo.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverLoginService {

    @Value("${naver.client_id}")
    private String clientId;
    @Value("${naver.client_secret}")
    private String clientSecret;
    @Value("${naver.redirect_url}")
    private String redirectUrl;


    public void callback(String code, String state) {

    }
}
