package com.dongyang.dongpo.service.rank;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.rank.RankDto;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankServiceTest {

    @InjectMocks
    private RankService rankService;

    @Mock
    private StoreReviewRepository reviewRepository;

    @Mock
    private StoreVisitCertRepository visitCertRepository;

    @Mock
    private StoreRepository storeRepository;

    private static final Pageable pageable = PageRequest.of(0, 10);

    @Test
    @DisplayName("방문인증 랭킹")
    void getVisitRank() {
        List<Object[]> mockVisitRank = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long count = (long) (Math.random() * 100 + 1L);
            mockVisitRank.add(new Object[]{mock(Member.class), count});
        }

        when(visitCertRepository.findTop10MembersBySuccessfulVisitCount(pageable)).thenReturn(mockVisitRank);

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
            mockVisitRank.add(new Object[]{mock(Member.class), count});
        }

        when(reviewRepository.findTop10MembersByReviewCount(pageable)).thenReturn(mockVisitRank);

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
            mockVisitRank.add(new Object[]{mock(Member.class), count});
        }

        when(storeRepository.findTop10MembersByStoreCount(pageable)).thenReturn(mockVisitRank);

        List<RankDto> rankDtos = rankService.getStoreRank();

        assertNotNull(rankDtos);
        assertEquals(10, rankDtos.size());

        verify(storeRepository).findTop10MembersByStoreCount(pageable);
    }
}