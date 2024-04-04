package com.dongyang.dongpo.controller.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController("/kakao")
public class KakaoAuthController {

    @Value("${kakao.restapi_key}")
    private String restapi_key;

    @Value("${kakao.redirect_url}")
    private String redirect_url;



}
