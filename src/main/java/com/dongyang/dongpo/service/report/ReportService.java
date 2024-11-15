package com.dongyang.dongpo.service.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.report.ReportReason;
import com.dongyang.dongpo.domain.report.ReviewReport;
import com.dongyang.dongpo.domain.report.StoreReport;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.report.ReportDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.report.ReviewReportRepository;
import com.dongyang.dongpo.repository.report.StoreReportRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
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

        storeRepository.save(store.addReport());

        StoreReport storeReport = request.toStoreEntity(member, store);
        storeReportRepository.save(storeReport);

        log.info("Member {} Report Store ID : {}", member.getId(), store.getId());
    }

    @Transactional
    public void addReviewReport(Long reviewId, Member member, ReportDto request){
        if (request.getReason() == ReportReason.ETC && (request.getText() == null || request.getText().isEmpty()))
            throw new CustomException(ErrorCode.REPORT_REASON_TEXT_REQUIRED);

        StoreReview review = storeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        storeReviewRepository.save(review.addReport());

        ReviewReport reviewReport = request.toReviewEntity(member, review);
        reviewReportRepository.save(reviewReport);

        log.info("Member {} Report Review ID : {}", member.getId(), review.getId());
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
