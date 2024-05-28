package com.dongyang.dongpo.service.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.report.ReviewReport;
import com.dongyang.dongpo.domain.report.StoreReport;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.report.ReportDto;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.exception.store.ReviewNotFoundException;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.report.ReviewReportRepository;
import com.dongyang.dongpo.repository.report.StoreReportRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final StoreReportRepository storeReportRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final StoreRepository storeRepository;
    private final StoreReviewRepository storeReviewRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional
    public void addStoreReport(Long storeId, String accessToken, ReportDto request) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        store.addReport();
        storeRepository.save(store);

        StoreReport storeReport = request.toStoreEntity(member, store);
        storeReportRepository.save(storeReport);
    }

    @Transactional
    public void addReviewReport(Long reviewId, String accessToken, ReportDto request) throws Exception {
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        StoreReview review = storeReviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);

        review.addReport();
        storeReviewRepository.save(review);

        ReviewReport reviewReport = request.toReviewEntity(member, review);
        reviewReportRepository.save(reviewReport);
    }
}
