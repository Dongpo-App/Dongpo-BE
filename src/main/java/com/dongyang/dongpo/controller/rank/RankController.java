package com.dongyang.dongpo.controller.rank;

import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.rank.RankDto;
import com.dongyang.dongpo.service.rank.RankService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/visit")
    @Operation(summary = "방문인증 랭킹")
    public ResponseEntity<ApiResponse<List<RankDto>>> getVisitRank() {
        return ResponseEntity.ok(new ApiResponse<>(rankService.getVisitRank()));
    }

    @GetMapping("/review")
    @Operation(summary = "리뷰 랭킹")
    public ResponseEntity<ApiResponse<List<RankDto>>> getReviewRank() {
        return ResponseEntity.ok(new ApiResponse<>(rankService.getReviewRank()));
    }

    @GetMapping("/store")
    @Operation(summary = "점포 등록 랭킹")
    public ResponseEntity<ApiResponse<List<RankDto>>> getStoreRank() {
        return ResponseEntity.ok(new ApiResponse<>(rankService.getStoreRank()));
    }
}
