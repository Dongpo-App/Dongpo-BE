package com.dongyang.dongpo.domain.report.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportDto;
import com.dongyang.dongpo.domain.report.entity.ReportReason;
import com.dongyang.dongpo.domain.report.entity.ReviewReport;
import com.dongyang.dongpo.domain.report.entity.StoreReport;
import com.dongyang.dongpo.domain.report.repository.ReviewReportRepository;
import com.dongyang.dongpo.domain.report.repository.StoreReportRepository;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreReview;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.store.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final StoreReportRepository storeReportRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final StoreRepository storeRepository;
    private final StoreReviewRepository storeReviewRepository;

    @Transactional
    public void addStoreReport(Long storeId, Member member, ReportDto request){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        store.addReportCount();

        StoreReport storeReport = request.toStoreEntity(member, store);
        storeReportRepository.save(storeReport);

        log.info("Member {} added Store Report - ID : {}", member.getEmail(), store.getId());
    }

    @Transactional
    public void addReviewReport(Long reviewId, Member member, ReportDto request){
        if (request.getReason() == ReportReason.ETC && (request.getText() == null || request.getText().isEmpty()))
            throw new CustomException(ErrorCode.REPORT_REASON_TEXT_REQUIRED);

        StoreReview review = storeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        review.addReportCount();

        ReviewReport reviewReport = request.toReviewEntity(member, review);
        reviewReportRepository.save(reviewReport);

        log.info("Member {} added Review Report - ID : {}", member.getEmail(), review.getId());
    }

    public List<ReportDto> myRegStoreReport(Member member){
        List<StoreReport> storeReportList = storeReportRepository.findByMemberId(member.getId());
        List<ReportDto> reportList = new ArrayList<>();

        if (storeReportList.isEmpty())
            return reportList;

        for (StoreReport storeReport : storeReportList)
            reportList.add(storeReport.toDto());

        return reportList;

    }

    public List<ReportDto> myRegReviewReport(Member member){
        List<ReviewReport> storeReportList = reviewReportRepository.findByMemberId(member.getId());
        List<ReportDto> reportList = new ArrayList<>();

        if (storeReportList.isEmpty())
            return reportList;

        for (ReviewReport reviewReport : storeReportList)
            reportList.add(reviewReport.toDto());

        return reportList;
    }

    public List<ReviewReport> findAllReviewReport() {
        return reviewReportRepository.findAll();
    }

    public ReviewReport findOneReviewReport(Long id){
        return reviewReportRepository.findById(id)
                .orElse(null);
    }

    public List<StoreReport> findAllStoreReport(){
        return storeReportRepository.findAll();
    }

    public StoreReport findOneStoreReport(Long id){
        return storeReportRepository.findById(id)
                .orElse(null);
    }
}
