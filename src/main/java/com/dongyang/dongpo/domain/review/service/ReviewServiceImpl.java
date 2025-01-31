package com.dongyang.dongpo.domain.review.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.review.dto.ReviewDto;
import com.dongyang.dongpo.domain.review.dto.ReviewRegisterRequestDto;
import com.dongyang.dongpo.domain.review.dto.ReviewResponseDto;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.review.entity.Review;
import com.dongyang.dongpo.domain.review.entity.ReviewPic;
import com.dongyang.dongpo.domain.review.enums.ReviewStatus;
import com.dongyang.dongpo.domain.store.repository.ReadOnlyStoreRepository;
import com.dongyang.dongpo.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewPicService reviewPicService;
    private final ReadOnlyStoreRepository storeRepository;
    private final TitleService titleService;

    // 리뷰 등록
    @Override
    public void addReview(final Member member, final Long storeId, final ReviewRegisterRequestDto reviewDto) {
        Store store = findStoreById(storeId);

        Review review = reviewRepository.save(reviewDto.toEntity(store, member));

        if (reviewDto.getReviewPics() != null && !reviewDto.getReviewPics().isEmpty())
            review.addReviewPics(reviewDto.getReviewPics());

        log.info("Member {} - added review on Store ID : {}", member.getEmail(), store.getId());

        Long count = reviewRepository.countByMember(member);
        if (count.equals(3L))
            titleService.addTitle(member, Title.REVIEW_PRO);
    }

    // 마이페이지: 내가 등록한 리뷰 조회
    @Override
    public List<ReviewDto> getMyReviews(final Member member) { // TODO: 페이징 구현
        return reviewRepository.findByMemberWithReviewPicsAndStore(member).stream()
                .filter(r -> r.getStatus().equals(ReviewStatus.VISIBLE))
                .map(Review::toMyPageResponse)
                .toList();
    }

    // 모든 리뷰 조회
    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // 특정 리뷰 조회
    @Override
    public ReviewDto findOne(final Long id) {
        return findReviewById(id).toResponse();
    }

    // 특정 점포의 리뷰들을 반환
    @Override
    public Page<ReviewResponseDto> getReviewsByStore(final Long storeId, final int page, final int size) {
        // 페이지 크기에 맞게 리뷰 Id 리스트를 페이징하여 조회 후, 해당 Id 리스트를 바탕으로 리뷰들을 상세 조회
        final Page<Long> reviewIds = reviewRepository.findReviewIdsByStoreAndPageRequest(
                findStoreById(storeId), PageRequest.of(page, size));

        if (reviewIds.isEmpty())
            throw new CustomException(ErrorCode.REVIEW_NOT_FOUND);

        return new PageImpl<>(reviewRepository.findReviewsWithDetailsByIdsDesc(reviewIds.getContent())
                .stream()
                .map(Review::toStoreReviewResponse)
                .toList(), reviewIds.getPageable(), reviewIds.getTotalElements());
    }

    // 최근 5개의 리뷰 사진 반환
    @Override
    public List<String> getLatestReviewPicsByStoreId(final Long id) {
        return reviewPicService.getTop5LatestReviewPics(id).stream()
                .map(ReviewPic::getPicUrl)
                .filter(Objects::nonNull)
                .toList();
    }

    // 리뷰 삭제
    @Override
    public void deleteReview(final Long storeId, final Long reviewId, final Member member) {
        findStoreById(storeId);

        Review review = findReviewById(reviewId);
        if (!review.getMember().getId().equals(member.getId()))
            throw new CustomException(ErrorCode.REVIEW_NOT_OWNED_BY_USER);

        review.delete();
        log.info("Deleted Review: {} by Member: {}", reviewId, member.getEmail());
    }

    // 리뷰 id로 조회
    private Review findReviewById(final Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }

    // 점포 id로 조회
    private Store findStoreById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

}
