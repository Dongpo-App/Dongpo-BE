package com.dongyang.dongpo.domain.rank;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.rank.dto.RankDto;
import com.dongyang.dongpo.domain.rank.service.RankService;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.review.repository.ReviewRepository;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankServiceTest {

    @InjectMocks
    private RankService rankService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreVisitCertRepository visitCertRepository;

    @Mock
    private StoreRepository storeRepository;

    private static final Pageable pageable = PageRequest.of(0, 10);
    private static final Member mockMember = mock(Member.class);

    @Test
    @DisplayName("방문인증 랭킹")
    void getVisitRank() {
        List<Object[]> mockVisitRank = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long count = (long) (Math.random() * 100 + 1L);
            mockVisitRank.add(new Object[]{mockMember, count});
        }

        when(visitCertRepository.findTop10MembersBySuccessfulVisitCount(pageable)).thenReturn(mockVisitRank);
        when(mockMember.getMainTitle()).thenReturn(mock(Title.class));

        List<RankDto> rankDtos = rankService.getVisitRank();

        assertNotNull(rankDtos);
        assertEquals(10, rankDtos.size());

        verify(visitCertRepository).findTop10MembersBySuccessfulVisitCount(pageable);
    }

    @Test
    @DisplayName("리뷰등록 랭킹")
    void getReviewRank() {
        List<Object[]> mockVisitRank = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long count = (long) (Math.random() * 100 + 1L);
            mockVisitRank.add(new Object[]{mockMember, count});
        }

        when(reviewRepository.findTop10MembersByReviewCount(pageable)).thenReturn(mockVisitRank);
        when(mockMember.getMainTitle()).thenReturn(mock(Title.class));

        List<RankDto> rankDtos = rankService.getReviewRank();

        assertNotNull(rankDtos);
        assertEquals(10, rankDtos.size());

        verify(reviewRepository).findTop10MembersByReviewCount(pageable);
    }

    @Test
    @DisplayName("점포등록 랭킹")
    void getStoreRank() {
        List<Object[]> mockVisitRank = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long count = (long) (Math.random() * 100 + 1L);
            mockVisitRank.add(new Object[]{mockMember, count});
        }

        when(storeRepository.findTop10MembersByStoreCount(pageable)).thenReturn(mockVisitRank);
        when(mockMember.getMainTitle()).thenReturn(mock(Title.class));

        List<RankDto> rankDtos = rankService.getStoreRank();

        assertNotNull(rankDtos);
        assertEquals(10, rankDtos.size());

        verify(storeRepository).findTop10MembersByStoreCount(pageable);
    }
}