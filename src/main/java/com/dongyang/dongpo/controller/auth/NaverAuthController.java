package com.dongyang.dongpo.controller.auth;

import com.dongyang.dongpo.service.auth.NaverLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/naver")
@RequiredArgsConstructor
public class NaverAuthController {

    private final NaverLoginService naverLoginService;

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code,
                         @RequestParam("state") String state) throws JsonProcessingException {

        naverLoginService.callback(code, state);
    }
}
