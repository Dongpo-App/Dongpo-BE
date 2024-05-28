package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.dto.auth.CodeRequest;
import com.dongyang.dongpo.service.auth.KakaoLoginService;
import com.dongyang.dongpo.service.auth.NaverLoginService;
import com.dongyang.dongpo.service.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/kakao")
    public ResponseEntity kakao(@RequestBody CodeRequest request) {
        return kakaoLoginService.kakaoCallBack(request);
    }

    @GetMapping("/naver")
    public ResponseEntity naver(@RequestBody CodeRequest request) {
        return naverLoginService.naverCallback(request);
    }

    @GetMapping("/apple")
    public ResponseEntity callback(){
        return null;
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT토큰 재발급")
    public ResponseEntity reissue(@RequestHeader("Authorization") String refreshToken) throws Exception {
        return tokenService.reissueAccessToken(refreshToken);
    }
}
