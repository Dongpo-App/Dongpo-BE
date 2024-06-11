package com.dongyang.dongpo.controller.mypage;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageDto>> getMyPageIndex(@RequestHeader("Authorization") String accessToken) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyPageIndex(accessToken)));
    }
}
