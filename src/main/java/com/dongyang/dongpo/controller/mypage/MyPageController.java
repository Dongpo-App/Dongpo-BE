package com.dongyang.dongpo.controller.mypage;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageDto>> getMyPageIndex(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyPageIndex(member)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateMyPageInfo(@AuthenticationPrincipal Member member, @RequestBody MyPageUpdateDto myPageUpdateDto) {
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);
        return ResponseEntity.ok().build();
    }
}
