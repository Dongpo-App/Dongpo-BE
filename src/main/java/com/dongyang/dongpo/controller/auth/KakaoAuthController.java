package com.dongyang.dongpo.controller.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
public class KakaoAuthController {

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code){
        return code;
    }
}
