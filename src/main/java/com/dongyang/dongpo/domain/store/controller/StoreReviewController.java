package com.dongyang.dongpo.domain.store.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.ReviewDto;
import com.dongyang.dongpo.domain.store.dto.StoreReviewResponseDto;
import com.dongyang.dongpo.domain.store.service.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "StoreReview API", description = "점포 리뷰 관련 API")
@RequestMapping("/api/stores/{storeId}/reviews")
public class StoreReviewController {

    private final StoreReviewService reviewService;

    @PostMapping
    @Operation(summary = "리뷰 등록")
    public ResponseEntity<ApiResponse<String>> addReview(@AuthenticationPrincipal Member member,
                                                         @PathVariable Long storeId,
                                                         @RequestBody ReviewDto reviewDto) {
        reviewService.addReview(member, storeId, reviewDto);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping
    @Operation(summary = "점포 리뷰 전체 조회")
    public ResponseEntity<ApiResponse<List<StoreReviewResponseDto>>> getReviewByStore(@PathVariable final Long storeId) {
        return ResponseEntity.ok(new ApiResponse<>(reviewService.getReviewsByStore(storeId)));
    }

    @GetMapping("/latest")
    @Operation(summary = "점포 리뷰 가장 최신 3건 조회")
    public ResponseEntity<ApiResponse<List<StoreReviewResponseDto>>> getLatestReviewsByStoreId(@PathVariable final Long storeId) {
        return ResponseEntity.ok(new ApiResponse<>(reviewService.getLatestReviewsByStoreId(storeId)));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable("storeId") Long storeId,
                                                            @PathVariable("reviewId") Long reviewId,
                                                            @AuthenticationPrincipal Member member) {
        reviewService.deleteReview(storeId, reviewId, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
}
