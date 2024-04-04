package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.member.SignService;
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
    private final SignService signService;

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code) throws JsonProcessingException {
        return oAuth2Service.kakaoCallBack(code);
    }

}
