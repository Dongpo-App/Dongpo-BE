package com.dongyang.dongpo.domain.review.service;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.dto.ReviewDto;
import com.dongyang.dongpo.domain.review.dto.ReviewRegisterRequestDto;
import com.dongyang.dongpo.domain.review.dto.ReviewResponseDto;
import com.dongyang.dongpo.domain.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    void addReview(Member member, Long storeId, ReviewRegisterRequestDto reviewRegisterRequestDto);

    List<ReviewDto> getMyReviews(Member member);

    List<Review> findAll();

    ReviewDto findOne(Long id);

    Page<ReviewResponseDto> getReviewsByStore(Long storeId, int page, int size);


    List<String> getLatestReviewPicsByStoreId(Long storeId);

    void deleteReview(Long storeId, Long reviewId, Member member);
}
