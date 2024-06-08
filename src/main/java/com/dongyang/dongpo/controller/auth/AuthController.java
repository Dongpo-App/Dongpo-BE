package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.JwtToken;
import com.dongyang.dongpo.dto.auth.SocialTokenDto;
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

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<JwtToken>> kakao(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(socialService.getKakaoUserInfo(token.getToken())));
    }

    @PostMapping("/naver")
    public ResponseEntity<ApiResponse<JwtToken>> naver(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(socialService.getNaverUserInfo(token.getToken())));
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
