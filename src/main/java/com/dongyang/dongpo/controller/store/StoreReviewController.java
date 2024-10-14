package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.service.store.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정")
    public ResponseEntity<ApiResponse<Boolean>> updateReview(@AuthenticationPrincipal Member member,
                                    @PathVariable Long reviewId,
                                    @RequestBody ReviewDto reviewDto) throws Exception {

        reviewService.updateReview(member, reviewId, reviewDto);
        return null;
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<ApiResponse<Boolean>> deleteReview(@AuthenticationPrincipal Member member,
                                    @PathVariable Long reviewId) throws Exception {

        reviewService.deleteReview(member, reviewId);
        return null;
    }

}
