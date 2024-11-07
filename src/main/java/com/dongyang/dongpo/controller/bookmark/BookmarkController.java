package com.dongyang.dongpo.controller.bookmark;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.bookmark.StoreBookmarkResponseDto;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import com.dongyang.dongpo.service.store.StoreService;
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
    private final StoreService storeService;

    @GetMapping("/{storeId}")
    @Operation(summary = "점포 북마크 정보 조회")
    public ResponseEntity<ApiResponse<StoreBookmarkResponseDto>> getStoreBookmarkInfo(@PathVariable Long storeId, @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.getStoreBookmarkInfo(storeId, member)));
    }

    @PostMapping("/{storeId}")
    @Operation(summary = "북마크 추가")
    public ResponseEntity<ApiResponse<StoreBookmarkResponseDto>> addBookmark(@PathVariable Long storeId, @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.addBookmark(member, storeId)));
    }

    @DeleteMapping("/{storeId}")
    @Operation(summary = "북마크 제거")
    public ResponseEntity<ApiResponse<StoreBookmarkResponseDto>> deleteBookmark(@PathVariable Long storeId, @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.deleteBookmark(storeId, member)));
    }
}
