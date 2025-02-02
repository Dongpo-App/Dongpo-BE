package com.dongyang.dongpo.domain.member.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.service.MyPageService;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<List<MyRegisteredStoresResponseDto>>> getMyRegisteredStores(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyRegisteredStores(member)));
    }

    @GetMapping("/titles")
    public ResponseEntity<ApiResponse<List<MyPageDto.TitleDto>>> getMyTitles(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyTitles(member)));
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<ApiResponse<Page<MyRegisteredBookmarksResponseDto>>> getMyBookmarks(@AuthenticationPrincipal Member member,
                                                                                              @RequestParam(value = "page", defaultValue = "0") @Min(0) int page) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyBookmarks(member, page)));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse<Page<MyRegisteredReviewsResponseDto>>> getMyReviews(@AuthenticationPrincipal final Member member,
                                                                                          @RequestParam(value = "page", defaultValue = "0") @Min(0) final int page) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyReviews(member, page)));
    }


    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateMyPageInfo(@AuthenticationPrincipal Member member, @RequestBody MyPageUpdateDto myPageUpdateDto) {
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);
        return ResponseEntity.ok().build();
    }
}
