package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
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
public class StoreReviewService {

    private final StoreReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void addReview(Member member, Long storeId, ReviewDto reviewDto) throws Exception{
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        StoreReview storeReview = reviewDto.toEntity(store, member);
        reviewRepository.save(storeReview);

        log.info("member {} add review store ID : {}", member.getId(), store.getId());
    }

    public List<ReviewDto> myRegReview(Long memberId){
       List<StoreReview> storeReviews =  reviewRepository.findByMemberId(memberId);
       List<ReviewDto> reviewDtos = new ArrayList<>();

       for (StoreReview review : storeReviews)
           reviewDtos.add(review.toResponse());

       return reviewDtos;
    }

    public List<StoreReview> findAll(){
        return reviewRepository.findAll();
    }

    public ReviewDto findOne(Long id) throws StoreNotFoundException {
        StoreReview review = reviewRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        return review.toResponse();
    }
}
