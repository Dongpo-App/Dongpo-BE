package com.dongyang.dongpo.domain.report.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportResponseDto;
import com.dongyang.dongpo.domain.report.dto.ReportRequestDto;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import com.dongyang.dongpo.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "신고 처리")
    public ResponseEntity<Void> addReport(@Valid @RequestBody final ReportRequestDto request,
                                          @AuthenticationPrincipal final Member member) {
        reportService.addReport(request, member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "나의 신고 내역 조회")
    public ResponseEntity<ApiResponse<Page<ReportResponseDto>>> myReports(@RequestParam("type") final ReportType type,
                                                                          @RequestParam("page") @Min(0) final int page,
                                                                          @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(reportService.getMyReports(member, type, page)));
    }

}
