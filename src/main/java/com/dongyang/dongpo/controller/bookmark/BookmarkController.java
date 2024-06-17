package com.dongyang.dongpo.controller.bookmark;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
                                                     @RequestHeader("Authorization") String accessToken) throws Exception {
        bookmarkService.addBookmark(accessToken, storeId);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping("")
    @Operation(summary = "북마크 조회")
    public ResponseEntity<ApiResponse<List<BookmarkDto>>> getBookmarks(@RequestHeader("Authorization") String accessToken) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(bookmarkService.bookmarkList(accessToken)));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBookmark(@PathVariable Long id,
                                                               @RequestHeader("Authorization") String accessToken) throws Exception {
        bookmarkService.deleteBookmark(id, accessToken);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
}
