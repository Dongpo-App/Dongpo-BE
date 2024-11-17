package com.dongyang.dongpo.controller.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.report.ReportDto;
import com.dongyang.dongpo.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/store/{storeId}")
    @Operation(summary = "점포 신고")
    public ResponseEntity<ApiResponse<String>> storeReport(@PathVariable("storeId") Long storeId,
                                                           @AuthenticationPrincipal Member member,
                                                           @RequestBody ReportDto request) {
        reportService.addStoreReport(storeId, member, request);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
    
    @PostMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 신고")
    public ResponseEntity<ApiResponse<String>> reviewReport(@PathVariable("reviewId") Long reviewId,
                                                            @AuthenticationPrincipal Member member,
                                                            @RequestBody ReportDto request) {
        reportService.addReviewReport(reviewId, member, request);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping("/store/me")
    @Operation(summary = "내가 신고한 점포조회")
    public ResponseEntity<ApiResponse<List<ReportDto>>> meReportStore(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(reportService.myRegStoreReport(member)));
    }

    @GetMapping("/review/me")
    @Operation(summary = "내가 신고한 리뷰조회")
    public ResponseEntity<ApiResponse<List<ReportDto>>> meReportReview(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(reportService.myRegReviewReport(member)));
    }
}
