package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.service.store.StoreReviewService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store/review")
@RequiredArgsConstructor
public class StoreReviewController {

    private final StoreReviewService reviewService;

    @PostMapping("/{storeId}")
    @Operation(summary = "리뷰 등록")
    public ResponseEntity addReview(@RequestHeader("Authorization") String accessToken,
                                    @PathVariable Long storeId,
                                    @RequestBody ReviewDto reviewDto) throws Exception {

        return reviewService.addReview(accessToken, storeId, reviewDto);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "내가 등록한 리뷰 조회")
    public ResponseEntity myRegReview(@PathVariable Long memberId) throws Exception {
        return reviewService.myRegReview(memberId);
    }
}
