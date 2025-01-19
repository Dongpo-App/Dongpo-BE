package com.dongyang.dongpo.domain.member.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkDto;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.service.MyPageService;
import com.dongyang.dongpo.domain.store.dto.ReviewDto;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
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
    public ResponseEntity<ApiResponse<List<MyRegisteredStoresResponseDto>>> getMyRegisteredStores(@AuthenticationPrincipal Member member) {
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

    @GetMapping("/reviews") // TODO: 페이징 구현
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getMyReviews(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(myPageService.getMyReviews(member)));
    }


    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateMyPageInfo(@AuthenticationPrincipal Member member, @RequestBody MyPageUpdateDto myPageUpdateDto) {
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);
        return ResponseEntity.ok().build();
    }
}
