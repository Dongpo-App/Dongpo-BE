package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.auth.*;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.service.auth.AppleLoginService;
import com.dongyang.dongpo.service.auth.SocialService;
import com.dongyang.dongpo.service.token.TokenService;
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
    public ResponseEntity<ApiResponse<?>> apple(@RequestBody AppleLoginDto appleLoginDto) {
        AppleLoginResponse response = appleLoginService.getAppleUserInfo(appleLoginDto);

        return response.getJwtToken() == null
                ? ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(response.getClaims(), ErrorCode.ADDITIONAL_INFO_REQUIRED_FOR_SIGNUP.toString()))
                : ResponseEntity.ok(new ApiResponse<>(response.getJwtToken()));
    }

    @PostMapping("/apple/continue")
    public ResponseEntity<ApiResponse<JwtToken>> appleSignupContinue(@RequestBody AppleSignupContinueDto appleSignupContinueDto) {
        return ResponseEntity.ok(new ApiResponse<>(appleLoginService.continueSignup(appleSignupContinueDto)));
    }

    @PostMapping("/apple/leave")
    public ResponseEntity<ApiResponse<String>> appleLeaveTest(@AuthenticationPrincipal Member member) {
        appleLoginService.revokeToken(member);
        return ResponseEntity.ok(new ApiResponse<>("Leave success."));
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT토큰 재발급")
    public ResponseEntity<ApiResponse<JwtToken>> reissue(@RequestBody JwtTokenReissueDto jwtTokenReissueDto) {
        return ResponseEntity.ok(new ApiResponse<>(tokenService.reissueAccessToken(jwtTokenReissueDto)));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ApiResponse<String>> logout(@AuthenticationPrincipal Member member,
                                                      @RequestHeader("Authorization") String authorization) {
        socialService.doLogout(member, authorization);
        return ResponseEntity.ok(new ApiResponse<>("Logout success."));
    }
}
