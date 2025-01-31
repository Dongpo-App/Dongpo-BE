package com.dongyang.dongpo.domain.rank.service;

import com.dongyang.dongpo.domain.rank.dto.RankDto;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.review.repository.ReviewRepository;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RankService {

    private final ReviewRepository reviewRepository;
    private final StoreVisitCertRepository visitCertRepository;
    private final StoreRepository storeRepository;

    private static final Pageable pageable = PageRequest.of(0, 10);

    public List<RankDto> getVisitRank() {
        List<Object[]> visitRank  = visitCertRepository.findTop10MembersBySuccessfulVisitCount(pageable);
        return visitRank.stream()
                .map(RankDto::toDto)
                .collect(Collectors.toList());
    }


    public List<RankDto> getReviewRank() {
        List<Object[]> reviewRanks = reviewRepository.findTop10MembersByReviewCount(pageable);
        return reviewRanks.stream()
                .map(RankDto::toDto)
                .collect(Collectors.toList());

    }

    public List<RankDto> getStoreRank() {
        List<Object[]> storeRank  = storeRepository.findTop10MembersByStoreCount(pageable);
        return storeRank.stream()
                .map(RankDto::toDto)
                .collect(Collectors.toList());
    }
}
