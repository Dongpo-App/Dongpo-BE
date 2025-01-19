package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.store.dto.ReviewDto;
import com.dongyang.dongpo.domain.store.dto.StoreReviewResponseDto;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreReview;
import com.dongyang.dongpo.domain.store.entity.StoreReviewPic;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.store.repository.StoreReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoreReviewService {

    private final StoreReviewRepository reviewRepository;
    private final StoreReviewPicService storeReviewPicService;
    private final StoreRepository storeRepository;
    private final TitleService titleService;

    @Transactional
    public void addReview(Member member, Long storeId, ReviewDto reviewDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreReview storeReview = reviewDto.toEntity(store, member);
        reviewRepository.save(storeReview);

        log.info("member {} add review store ID : {}", member.getId(), store.getId());

        Long count = reviewRepository.countByMember(member);
        if (count.equals(3L))
            titleService.addTitle(member, Title.REVIEW_PRO);

    }

    public List<ReviewDto> getMyReviews(Member member) { // TODO: 페이징 구현
        return reviewRepository.findByMemberWithReviewPicsAndStore(member).stream()
                .filter(r -> r.getStatus().equals(StoreReview.ReviewStatus.VISIBLE))
                .map(StoreReview::toMyPageResponse)
                .toList();
    }

    public List<StoreReview> findAll() {
        return reviewRepository.findAll();
    }

    public ReviewDto findOne(Long id) {
        StoreReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        return review.toResponse();
    }

    public List<StoreReviewResponseDto> getReviewsByStore(final Long storeId) {
        if (!storeRepository.existsById(storeId))
            throw new CustomException(ErrorCode.STORE_NOT_FOUND); // 추후 의존성 수정

        // 추후 페이징 구현
        return reviewRepository.findReviewWithDetailsByStoreDesc(storeId).stream()
                .map(StoreReview::toStoreReviewResponse)
                .toList();
    }

    // 최신 3개의 리뷰 반환
    public List<StoreReviewResponseDto> getLatestReviewsByStoreId(final Long storeId) {
        final List<Long> top3LatestReviewsByStoreId = reviewRepository.findTop3LatestReviewIdsByStoreId(storeId, PageRequest.ofSize(3));
        if (top3LatestReviewsByStoreId.isEmpty())
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);

        return reviewRepository.findReviewsWithPicsByIds(top3LatestReviewsByStoreId).stream()
                .map(StoreReview::toStoreReviewResponse)
                .toList();
    }

    // 최근 5개의 리뷰 사진 반환
    public List<String> getLatestReviewPicsByStoreId(Long id) {
        return storeReviewPicService.getTop5LatestReviewPics(id).stream()
                .map(StoreReviewPic::getPicUrl)
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public void deleteReview(Member member, Long reviewId) {
        StoreReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Member reviewMember = review.getMember();

        if (!reviewMember.getId().equals(member.getId()))
            throw new CustomException(ErrorCode.REVIEW_NOT_OWNED_BY_USER);

        review.delete();
        log.info("Deleted Review: {} by Member: {}", reviewId, member.getEmail());
    }

}
