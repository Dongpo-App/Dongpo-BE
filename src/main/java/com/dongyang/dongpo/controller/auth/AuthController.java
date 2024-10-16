package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.auth.AppleLoginDto;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.JwtTokenReissueDto;
import com.dongyang.dongpo.dto.auth.SocialTokenDto;
import com.dongyang.dongpo.service.auth.AppleLoginService;
import com.dongyang.dongpo.service.auth.SocialService;
import com.dongyang.dongpo.service.token.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SocialService socialService;
    private final TokenService tokenService;
    private final AppleLoginService appleLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<JwtToken>> kakao(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(socialService.getKakaoUserInfo(token.getToken())));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<JwtToken>> kakaoCallback(@RequestParam("code") String AccessCode) {
        return ResponseEntity.ok(new ApiResponse<>(socialService.kakaoCallback(AccessCode)));
    }

    @PostMapping("/naver")
    public ResponseEntity<ApiResponse<JwtToken>> naver(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(socialService.getNaverUserInfo(token.getToken())));
    }

    @PostMapping("/apple")
    public ResponseEntity<ApiResponse<JwtToken>> apple(@RequestBody AppleLoginDto appleLoginDto) {
        return ResponseEntity.ok(new ApiResponse<>(appleLoginService.getAppleUserInfo(appleLoginDto)));
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT토큰 재발급")
    public ResponseEntity<ApiResponse<JwtToken>> reissue(@RequestBody JwtTokenReissueDto jwtTokenReissueDto) {
        return ResponseEntity.ok(new ApiResponse<>(tokenService.reissueAccessToken(jwtTokenReissueDto)));
    }
}
