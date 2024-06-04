package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.SocialTokenDto;
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

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<JwtToken>> callback(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(kakaoLoginService.getKakaoUserInfo(token.getToken())));
    }


    @GetMapping("/naver")
    public ResponseEntity<ApiResponse<JwtToken>> callback(@RequestParam("code") String code,
                                   @RequestParam("state") String state) {

        return ResponseEntity.ok(new ApiResponse<>(naverLoginService.naverCallback(code, state)));
    }

    @GetMapping("/apple/callback")
    public ResponseEntity callback(){
        return null;
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT토큰 재발급")
    public ResponseEntity<ApiResponse<JwtToken>> reissue(@RequestHeader("Authorization") String refreshToken) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(tokenService.reissueAccessToken(refreshToken)));
    }
}
