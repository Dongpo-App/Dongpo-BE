package com.dongyang.dongpo.domain.auth.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.auth.dto.*;
import com.dongyang.dongpo.domain.auth.service.AuthService;
import com.dongyang.dongpo.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<ApiResponse<JwtToken>> kakao(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(authService.handleLogin(token.getToken())));
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<ApiResponse<JwtToken>> kakaoCallback(@RequestParam("code") String accessCode) {
        return ResponseEntity.ok(new ApiResponse<>(authService.kakaoCallback(accessCode)));
    }

    /*
    @PostMapping("/naver")
    public ResponseEntity<ApiResponse<JwtToken>> naver(@RequestBody SocialTokenDto token) {
        return ResponseEntity.ok(new ApiResponse<>(authService.getNaverUserInfo(token.getToken())));
    }
     */

    @PostMapping("/apple")
    public ResponseEntity<ApiResponse<?>> apple(@RequestBody AppleLoginDto appleLoginDto) {
        AppleLoginResponse response = authService.handleAppleLogin(appleLoginDto);

        return response.getJwtToken() == null
                ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(response.getClaims(), ErrorCode.ADDITIONAL_INFO_REQUIRED_FOR_SIGNUP.toString()))
                : ResponseEntity.ok(new ApiResponse<>(response.getJwtToken()));
    }

    @PostMapping("/apple/continue")
    public ResponseEntity<ApiResponse<JwtToken>> appleSignupContinue(@RequestBody AppleSignupContinueDto appleSignupContinueDto) {
        return ResponseEntity.ok(new ApiResponse<>(authService.continueAppleSignup(appleSignupContinueDto)));
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT토큰 재발급")
    public ResponseEntity<ApiResponse<JwtToken>> reissue(@RequestBody JwtTokenReissueDto jwtTokenReissueDto) {
        return ResponseEntity.ok(new ApiResponse<>(authService.reissueAccessToken(jwtTokenReissueDto)));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal Member member) {
        authService.logout(member);
        return ResponseEntity.ok(new ApiResponse<>("Logout success."));
    }

    @PostMapping("/leave")
    @Operation(summary = "회원탈퇴")
    public ResponseEntity<ApiResponse<String>> leave(@AuthenticationPrincipal Member member) {
        authService.withdraw(member);
        return ResponseEntity.ok(new ApiResponse<>("Leave success."));
    }
}
