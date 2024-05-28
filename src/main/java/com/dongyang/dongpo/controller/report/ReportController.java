package com.dongyang.dongpo.controller.report;

import com.dongyang.dongpo.dto.report.ReportDto;
import com.dongyang.dongpo.service.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/store/{storeId}")
    @Operation(summary = "점포 신고")
    public ResponseEntity storeReport(@PathVariable("storeId") Long storeId,
                                      @RequestHeader("Authorization") String accessToken,
                                      @RequestBody ReportDto request) throws Exception {
        reportService.addStoreReport(storeId, accessToken, request);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 신고")
    public ResponseEntity reviewReport(@PathVariable("reviewId") Long reviewId,
                                       @RequestHeader("Authorization") String accessToken,
                                       @RequestBody ReportDto request) throws Exception {
        reportService.addReviewReport(reviewId, accessToken, request);
        return ResponseEntity.ok().build();
    }
}
