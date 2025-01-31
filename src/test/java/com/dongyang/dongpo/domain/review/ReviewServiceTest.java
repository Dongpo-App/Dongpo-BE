package com.dongyang.dongpo.domain.review;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.entity.Review;
import com.dongyang.dongpo.domain.review.dto.ReviewDto;
import com.dongyang.dongpo.domain.review.enums.ReviewStatus;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.review.repository.ReviewRepository;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.review.service.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private TitleService titleService;

    @InjectMocks
    private ReviewServiceImpl storeReviewService;

    @Test
    void addReview() {
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        Review review1 = mock(Review.class);
        Review review2 = mock(Review.class);
        ReviewDto reviewDto1 = mock(ReviewDto.class);
        ReviewDto reviewDto2 = mock(ReviewDto.class);

        when(reviewRepository.findByMemberWithReviewPicsAndStore(member)).thenReturn(List.of(review1, review2));
        when(review1.toMyPageResponse()).thenReturn(reviewDto1);
        when(review2.toMyPageResponse()).thenReturn(reviewDto2);
        when(review1.getStatus()).thenReturn(ReviewStatus.VISIBLE);
        when(review2.getStatus()).thenReturn(ReviewStatus.VISIBLE);

        // when
        List<ReviewDto> reviewDtos = storeReviewService.getMyReviews(member);

        // then
        assertThat(reviewDtos).hasSize(2);
        assertThat(reviewDtos).containsExactly(reviewDto1, reviewDto2);
        verify(reviewRepository).findByMemberWithReviewPicsAndStore(member);
    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }
}