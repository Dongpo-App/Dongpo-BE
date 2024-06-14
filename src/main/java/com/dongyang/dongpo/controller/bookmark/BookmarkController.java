package com.dongyang.dongpo.controller.bookmark;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
