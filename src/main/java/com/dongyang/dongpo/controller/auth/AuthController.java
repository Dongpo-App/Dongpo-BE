package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.auth.KakaoLoginService;
import com.dongyang.dongpo.service.auth.NaverLoginService;
import com.dongyang.dongpo.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;
    private final TokenService tokenService;

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

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestHeader("Authorization") String refreshToken) throws Exception {
        return tokenService.reissueAccessToken(refreshToken);
    }

    @GetMapping("/test")
    public String test(){
        return "tㅓㅇ공";
    }
}
