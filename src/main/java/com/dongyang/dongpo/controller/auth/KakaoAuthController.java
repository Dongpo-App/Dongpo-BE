package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.auth.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/callback")
    public ResponseEntity callback(@RequestParam("code") String code) {
        return kakaoLoginService.kakaoCallBack(code);
    }
}
