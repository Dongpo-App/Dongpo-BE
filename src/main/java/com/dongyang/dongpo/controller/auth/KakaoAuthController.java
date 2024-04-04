package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.oauth2.OAuth2Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final OAuth2Service oAuth2Service;

    @GetMapping("/callback")
    public Mono<String> callback(@RequestParam("code") String code) throws JsonProcessingException {
        return oAuth2Service.kakaoCallBack(code).block();
    }
}
