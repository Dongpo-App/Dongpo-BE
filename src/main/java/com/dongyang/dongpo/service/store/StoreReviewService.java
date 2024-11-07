package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreReviewResponseDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewPicRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.service.title.TitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoreReviewService {

    private final StoreReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final TitleService titleService;
    private final StoreReviewPicRepository storeReviewPicRepository;

    @Transactional
    public void addReview(Member member, Long storeId, ReviewDto reviewDto){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreReview storeReview = reviewDto.toEntity(store, member);
        reviewRepository.save(storeReview);

        if (reviewDto.getReviewPics() != null) { // 리뷰 사진이 첨부 되지 않았을 경우
            reviewDto.getReviewPics().forEach(picUrl -> {
                StoreReviewPic pic = StoreReviewPic.builder()
                        .picUrl(picUrl)
                        .reviewId(storeReview)
                        .store(store)
                        .build();
                storeReviewPicRepository.save(pic);
            });
        }

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

    public List<StoreReviewResponseDto> getReviewByStore(final Long storeId) {
        if (!storeRepository.existsById(storeId))
            throw new CustomException(ErrorCode.STORE_NOT_FOUND); // 추후 의존성 수정

        return reviewRepository.findReviewWithDetailsByStoreDesc(storeId).stream()
                .map(StoreReview::toStoreReviewResponse)
                .toList();
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
}
