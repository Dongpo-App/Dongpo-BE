package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.service.member.SignService;
import com.dongyang.dongpo.service.auth.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/callback")
    public UserInfo callback(@RequestParam("code") String code) {
        return kakaoLoginService.kakaoCallBack(code);
    }
}
