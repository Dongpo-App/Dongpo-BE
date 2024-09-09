package com.dongyang.dongpo.service.rank;

import com.dongyang.dongpo.dto.rank.RankDto;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
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


    private final StoreReviewRepository reviewRepository;
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
