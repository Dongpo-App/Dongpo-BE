package com.dongyang.dongpo.controller.bookmark;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("")
    @Operation(summary = "북마크 추가")
    public ResponseEntity<ApiResponse<String>> addBookmark(@RequestBody Map<String, Long> request,
                                                           @AuthenticationPrincipal Member member) {
        bookmarkService.addBookmark(member, request.get("storeId"));
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @DeleteMapping("/{storeId}")
    @Operation(summary = "북마크 제거")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(@PathVariable Long storeId,
                                                              @AuthenticationPrincipal Member member) {
        bookmarkService.deleteBookmark(storeId, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
}
