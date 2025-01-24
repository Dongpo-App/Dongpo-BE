package com.dongyang.dongpo.domain.store.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.ReviewRegisterRequestDto;
import com.dongyang.dongpo.domain.store.dto.StoreReviewResponseDto;
import com.dongyang.dongpo.domain.store.service.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<ApiResponse<String>> addReview(@AuthenticationPrincipal final Member member,
                                                         @PathVariable @Min(1) final Long storeId,
                                                         @Valid @RequestBody final ReviewRegisterRequestDto reviewDto) {
        reviewService.addReview(member, storeId, reviewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "점포 리뷰 전체 조회")
    public ResponseEntity<ApiResponse<List<StoreReviewResponseDto>>> getReviewByStore(@PathVariable @Min(1) final Long storeId) {
        return ResponseEntity.ok(new ApiResponse<>(reviewService.getReviewsByStore(storeId)));
    }

    @GetMapping("/latest")
    @Operation(summary = "점포 리뷰 가장 최신 3건 조회")
    public ResponseEntity<ApiResponse<List<StoreReviewResponseDto>>> getLatestReviewsByStoreId(@PathVariable @Min(1) final Long storeId) {
        return ResponseEntity.ok(new ApiResponse<>(reviewService.getLatestReviewsByStoreId(storeId)));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable("storeId") @Min(1) final Long storeId,
                                                            @PathVariable("reviewId") @Min(1) final Long reviewId,
                                                            @AuthenticationPrincipal final Member member) {
        reviewService.deleteReview(storeId, reviewId, member);
        return ResponseEntity.ok().build();
    }
}
