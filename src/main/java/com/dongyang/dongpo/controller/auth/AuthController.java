package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.auth.KakaoLoginService;
import com.dongyang.dongpo.service.auth.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;

    @GetMapping("/kakao/callback")
    public ResponseEntity callback(@RequestParam("code") String code) {
        return kakaoLoginService.kakaoCallBack(code);
    }

    @GetMapping("/naver/callback")
    public ResponseEntity callback(@RequestParam("code") String code,
                                   @RequestParam("state") String state) {

        return naverLoginService.naverCallback(code, state);
    }

    @GetMapping("/apple/callback")
    public ResponseEntity callback(){
        return null;
    }
}
