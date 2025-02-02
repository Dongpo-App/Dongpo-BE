package com.dongyang.dongpo.domain.review;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.review.entity.Review;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.review.repository.ReviewRepository;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.review.service.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    private ReviewServiceImpl reviewService;

    @Test
    void addReview() {
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<Long> reviewIds = new PageImpl<>(List.of(1L, 2L), pageRequest, 2);
        Review review1 = mock(Review.class);
        Review review2 = mock(Review.class);
        MyRegisteredReviewsResponseDto reviewDto1 = mock(MyRegisteredReviewsResponseDto.class);
        MyRegisteredReviewsResponseDto reviewDto2 = mock(MyRegisteredReviewsResponseDto.class);

        when(reviewRepository.findReviewIdsByMemberAndPageRequest(member, pageRequest)).thenReturn(reviewIds);
        when(reviewRepository.findMyRegisteredReviewsByReviewIds(List.of(1L, 2L))).thenReturn(List.of(review1, review2));
        when(review1.toMyRegisteredReviewsResponse()).thenReturn(reviewDto1);
        when(review2.toMyRegisteredReviewsResponse()).thenReturn(reviewDto2);

        // when
        Page<MyRegisteredReviewsResponseDto> result = reviewService.getMyReviews(member, 0);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(reviewDto1, reviewDto2);
        verify(reviewRepository).findReviewIdsByMemberAndPageRequest(member, pageRequest);
        verify(reviewRepository).findMyRegisteredReviewsByReviewIds(List.of(1L, 2L));
    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }
}