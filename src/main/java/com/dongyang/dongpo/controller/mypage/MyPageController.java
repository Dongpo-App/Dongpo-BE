package com.dongyang.dongpo.controller.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreSummaryDto;
import com.dongyang.dongpo.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ApiResponse<MyPageDto>> getMyPageIndex(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyPageIndex(member)));
    }

    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<List<StoreSummaryDto>>> getMyRegisteredStores(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyRegisteredStores(member)));
    }

    @GetMapping("/titles")
    public ResponseEntity<ApiResponse<List<MyPageDto.TitleDto>>> getMyTitles(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyTitles(member)));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<List<BookmarkDto>>> getMyBookmarks(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyBookmarks(member)));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getMyReviews(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyReviews(member)));
    }


    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateMyPageInfo(@AuthenticationPrincipal Member member, @RequestBody MyPageUpdateDto myPageUpdateDto) {
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);
        return ResponseEntity.ok().build();
    }
}
