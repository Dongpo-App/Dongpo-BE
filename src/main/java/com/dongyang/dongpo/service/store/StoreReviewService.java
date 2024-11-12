package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreReviewResponseDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import com.dongyang.dongpo.service.title.TitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoreReviewService {

    private final StoreReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final StoreVisitCertRepository storeVisitCertRepository;
    private final TitleService titleService;

    @Transactional
    public void addReview(Member member, Long storeId, ReviewDto reviewDto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreReview storeReview = reviewDto.toEntity(store, member);
        reviewRepository.save(storeReview);

        log.info("member {} add review store ID : {}", member.getId(), store.getId());

        Long count = reviewRepository.countByMember(member);
        if (count.equals(3L))
            titleService.addTitle(member, Title.REVIEW_PRO);

    }

    public List<ReviewDto> getMyReviews(Member member) {
        return reviewRepository.findByMemberWithReviewPicsAndStore(member).stream()
                .filter(r -> r.getStatus().equals(StoreReview.ReviewStatus.VISIBLE))
                .map(StoreReview::toMyPageResponse)
                .toList();
    }

    public List<StoreReview> findAll(){
        return reviewRepository.findAll();
    }

    public ReviewDto findOne(Long id){
        StoreReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        return review.toResponse();
    }

    public List<StoreReviewResponseDto> getReviewsByStore(final Long storeId) {
        if (!storeRepository.existsById(storeId))
            throw new CustomException(ErrorCode.STORE_NOT_FOUND); // 추후 의존성 수정

        return reviewRepository.findReviewWithDetailsByStoreDesc(storeId).stream()
                .map(StoreReview::toStoreReviewResponse)
                .toList();
    }

    public List<String> getReviewPicsByStoreId(Long id) {
        return reviewRepository.findByStoreId(id).stream()
                .flatMap(storeReview -> storeReview.getReviewPics().stream())
                .map(StoreReviewPic::getPicUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Member member, Long reviewId) {
        StoreReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Member reviewMember = review.getMember();

        if (!reviewMember.getId().equals(member.getId()))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        review.delete();
        log.info("Deleted Review: {} by Member: {}", reviewId, member.getEmail());
    }

	public Boolean checkPossibleAddReview(Member member, Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreVisitCert storeVisitCert = storeVisitCertRepository
            .findTopByStoreAndMemberAndIsVisitSuccessfulTrueOrderByCertDateDesc(store, member)
            .orElseThrow(() -> new CustomException(ErrorCode.VISIT_CERT_NOT_FOUND));

        return storeVisitCert.isPossibleAddReview();
    }
}
