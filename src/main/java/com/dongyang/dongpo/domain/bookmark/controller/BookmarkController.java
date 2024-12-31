package com.dongyang.dongpo.domain.bookmark.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkResponseDto;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{storeId}")
    @Operation(summary = "북마크 추가")
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> addBookmark(@PathVariable final Long storeId,
                                                                        @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.addBookmark(member, storeId)));
    }

    @DeleteMapping("/{storeId}")
    @Operation(summary = "북마크 제거")
    public ResponseEntity<ApiResponse<BookmarkResponseDto>> deleteBookmark(@PathVariable final Long storeId,
                                                              @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.deleteBookmark(storeId, member)));
    }
}
