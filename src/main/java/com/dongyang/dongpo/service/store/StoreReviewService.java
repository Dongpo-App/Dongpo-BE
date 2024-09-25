package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
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

import java.util.ArrayList;
import java.util.List;

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
        List<ReviewDto> reviewDtos = new ArrayList<>();
        reviewRepository.findByMember(member).forEach(storeReview -> {
            reviewDtos.add(ReviewDto.toDto(storeReview));
        });
        return reviewDtos;
    }

    public List<StoreReview> findAll(){
        return reviewRepository.findAll();
    }

    public ReviewDto findOne(Long id){
        StoreReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        return review.toResponse();
    }
}
