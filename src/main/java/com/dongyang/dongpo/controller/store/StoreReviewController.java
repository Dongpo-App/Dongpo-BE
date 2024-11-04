package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.service.store.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store/review")
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService reviewService;

    @PostMapping("/{storeId}")
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<ApiResponse<String>> addReview(@AuthenticationPrincipal Member member,
                                    @PathVariable Long storeId,
                                    @RequestBody ReviewDto reviewDto) throws Exception {

        reviewService.addReview(member, storeId, reviewDto);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<ApiResponse<String>> deleteReview(@AuthenticationPrincipal Member member,
                                    @PathVariable Long reviewId) {

        reviewService.deleteReview(member, reviewId);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

}
