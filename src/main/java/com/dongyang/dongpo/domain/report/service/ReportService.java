package com.dongyang.dongpo.domain.report.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportResponseDto;
import com.dongyang.dongpo.domain.report.dto.ReportRequestDto;
import com.dongyang.dongpo.domain.report.entity.Report;
import com.dongyang.dongpo.domain.report.enums.ReportReason;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import com.dongyang.dongpo.domain.report.repository.ReportRepository;
import com.dongyang.dongpo.domain.review.service.ReviewService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportService {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ReportRepository reportRepository;
    private final StoreService storeService;
    private final ReviewService reviewService;

    public void addReport(final ReportRequestDto request, final Member member) {
        if (request.getReason() == ReportReason.ETC && (request.getText() == null || request.getText().isEmpty()))
            throw new CustomException(ErrorCode.REPORT_REASON_TEXT_REQUIRED);

        switch (request.getType()) {
            case STORE:
                storeService.findById(request.getTargetId()).addReportCount();
                break;
            case REVIEW:
                reviewService.findReviewById(request.getTargetId()).addReportCount();
                break;
            default:
                throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);
        }

        reportRepository.save(request.toEntity(member));
        log.info("Member {} added {} Report - ID : {}", member.getEmail(), request.getType(), request.getTargetId());
    }

    public Page<ReportResponseDto> getMyReports(final Member member, final ReportType type, final int page) {
        Page<Report> byMemberAndType = reportRepository.findByMemberAndType(member, type, PageRequest.of(page, DEFAULT_PAGE_SIZE));

        if (byMemberAndType.isEmpty())
            throw new CustomException(ErrorCode.REPORTS_REGISTERED_BY_MEMBER_NOT_FOUND);

        return new PageImpl<>(byMemberAndType.map(Report::toResponseDto).getContent(),
                byMemberAndType.getPageable(),
                byMemberAndType.getTotalElements());
    }

    public List<Report> findAllReviewReport() {
        return reportRepository.findByType(ReportType.REVIEW);
    }

    public Report findOneReviewReport(final Long id) {
        return reportRepository.findById(id)
                .orElse(null);
    }

    public List<Report> findAllStoreReport() {
        return reportRepository.findByType(ReportType.STORE);
    }

    public Report findOneStoreReport(final Long id) {
        return reportRepository.findById(id)
                .orElse(null);
    }
}
