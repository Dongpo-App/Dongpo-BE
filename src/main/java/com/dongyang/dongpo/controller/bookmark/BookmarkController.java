package com.dongyang.dongpo.controller.bookmark;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("")
    @Operation(summary = "북마크 추가")
    public ResponseEntity<ApiResponse<String>> addBookmark(@RequestBody Long storeId,
                                                           @AuthenticationPrincipal Member member) throws Exception {
        bookmarkService.addBookmark(member, storeId);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping("")
    @Operation(summary = "북마크 조회")
    public ResponseEntity<ApiResponse<List<BookmarkDto>>> getBookmarks(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.bookmarkList(member)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(@PathVariable Long id,
                                                              @AuthenticationPrincipal Member member) throws Exception {
        bookmarkService.deleteBookmark(id, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
}
