package com.dongyang.dongpo.domain.report.service;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportRequestDto;
import com.dongyang.dongpo.domain.report.dto.ReportResponseDto;
import com.dongyang.dongpo.domain.report.entity.Report;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    void addReport(ReportRequestDto request, Member member);

    Page<ReportResponseDto> getMyReports(Member member, ReportType type, int page);

    List<Report> findAllReviewReport();

    Report findOneReviewReport(Long reviewId);

    List<Report> findAllStoreReport();

    Report findOneStoreReport(Long storeId);
}
