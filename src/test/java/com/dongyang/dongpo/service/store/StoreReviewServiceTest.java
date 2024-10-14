package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.service.title.TitleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreReviewServiceTest {

    @Mock
    private StoreReviewRepository storeReviewRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private TitleService titleService;

    @InjectMocks
    private StoreReviewService storeReviewService;

    @Test
    void addReview() {
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        StoreReview review1 = mock(StoreReview.class);
        StoreReview review2 = mock(StoreReview.class);
        ReviewDto reviewDto1 = mock(ReviewDto.class);
        ReviewDto reviewDto2 = mock(ReviewDto.class);

        when(storeReviewRepository.findByMemberWithReviewPicsAndStore(member)).thenReturn(List.of(review1, review2));
        when(review1.toMyPageResponse()).thenReturn(reviewDto1);
        when(review2.toMyPageResponse()).thenReturn(reviewDto2);
        when(review1.getStatus()).thenReturn(StoreReview.ReviewStatus.VISIBLE);
        when(review2.getStatus()).thenReturn(StoreReview.ReviewStatus.VISIBLE);

        // when
        List<ReviewDto> reviewDtos = storeReviewService.getMyReviews(member);

        // then
        assertThat(reviewDtos).hasSize(2);
        assertThat(reviewDtos).containsExactly(reviewDto1, reviewDto2);
        verify(storeReviewRepository).findByMemberWithReviewPicsAndStore(member);
    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }
}