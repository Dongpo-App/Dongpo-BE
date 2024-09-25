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
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        StoreReview review1 = mock(StoreReview.class);
        StoreReview review2 = mock(StoreReview.class);
        ReviewDto reviewDto1 = mock(ReviewDto.class);
        ReviewDto reviewDto2 = mock(ReviewDto.class);
        Integer reportCount1 = 5;
        Integer reportCount2 = 3;

        when(storeReviewRepository.findByMember(member)).thenReturn(List.of(review1, review2));
        when(review1.getMember()).thenReturn(member);
        when(review2.getMember()).thenReturn(member);
        when(review1.getStore()).thenReturn(store1);
        when(review2.getStore()).thenReturn(store2);
        when(review1.getReportCount()).thenReturn(reportCount1);
        when(review2.getReportCount()).thenReturn(reportCount2);
        when(ReviewDto.toDto(review1)).thenCallRealMethod();
        when(ReviewDto.toDto(review2)).thenCallRealMethod();

        // when
        List<ReviewDto> reviewDtos = storeReviewService.getMyReviews(member);

        // then
        assertThat(reviewDtos).hasSize(2);
        verify(storeReviewRepository).findByMember(member);
    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }
}