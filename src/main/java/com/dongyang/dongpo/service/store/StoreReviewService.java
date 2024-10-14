package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
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

    public List<String> getReviewPicsByStoreId(Long id) {
        return reviewRepository.findByStoreId(id).stream()
                .flatMap(storeReview -> storeReview.getReviewPics().stream())
                .map(StoreReviewPic::getPicUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
	public void updateReview(Member member, Long reviewId, ReviewDto reviewDto) {
        StoreReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().equals(member))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        List<StoreReviewPic> reviewPics = review.getReviewPics();

        if (reviewPics != null && !reviewDto.getReviewPics().isEmpty()) {
            review.clearReviewPics();

            reviewDto.getReviewPics().forEach(picUrl -> {
                StoreReviewPic pic = StoreReviewPic.builder()
                    .picUrl(picUrl)
                    .build();
                review.addReviewPic(pic);
            });
        }

        review.update(reviewDto, reviewPics);

        log.info("Updated Review: {} by Member: {}", reviewId, member.getEmail());
    }

    @Transactional
    public void deleteReview(Member member, Long reviewId) {
        StoreReview review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().equals(member))
            throw new CustomException(ErrorCode.UNAUTHORIZED);

        review.delete();
        log.info("Deleted Review: {} by Member: {}", reviewId, member.getEmail());
    }
}
