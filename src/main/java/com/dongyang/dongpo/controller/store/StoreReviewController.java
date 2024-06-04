package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.service.store.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store/review")
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService reviewService;

    @PostMapping("/{storeId}")
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<ApiResponse<String>> addReview(@RequestHeader("Authorization") String accessToken,
                                    @PathVariable Long storeId,
                                    @RequestBody ReviewDto reviewDto) throws Exception {

        reviewService.addReview(accessToken, storeId, reviewDto);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "내가 등록한 리뷰 조회")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> myRegReview(@PathVariable Long memberId) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(reviewService.myRegReview(memberId)));
    }
}
