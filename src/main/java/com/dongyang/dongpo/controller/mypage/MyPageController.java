package com.dongyang.dongpo.controller.mypage;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageDto>> getMyPageIndex(@RequestHeader("Authorization") String accessToken) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyPageIndex(accessToken)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateMyPageInfo(@RequestHeader("Authorization") String accessToken, @RequestBody MyPageUpdateDto myPageUpdateDto) {
        myPageService.updateMyPageInfo(accessToken, myPageUpdateDto);
        return ResponseEntity.ok().build();
    }
}
