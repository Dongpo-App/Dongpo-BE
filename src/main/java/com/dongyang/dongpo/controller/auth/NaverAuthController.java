package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.auth.NaverLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/naver")
@RequiredArgsConstructor
public class NaverAuthController {

    private final NaverLoginService naverLoginService;

    @GetMapping("/callback")
    public ResponseEntity callback(@RequestParam("code") String code,
                                   @RequestParam("state") String state) {

        return naverLoginService.naverCallback(code, state);
    }
}
