package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreVisitCert;
import com.dongyang.dongpo.domain.store.enums.TimeRange;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreVisitCertServiceImpl implements StoreVisitCertService {

    private final StoreVisitCertRepository storeVisitCertRepository;
    private final StoreUtil storeUtil;

    // 방문인증 내역 추가
    @Override
    public void addStoreVisitCert(final Store store, final Member member, final Boolean isVisitSuccessful) {
        final LocalDateTime now = LocalDateTime.now();
        storeVisitCertRepository.save(StoreVisitCert.builder()
                .store(store)
                .member(member)
                .isVisitSuccessful(isVisitSuccessful)
                .certDate(now)
                .certDay(now.getDayOfWeek())
                .certTimeRange(storeUtil.getTimeRange(now.getHour()))
                .build());
    }

    // 현재 시간대, 요일에 방문 인증 성공한 횟수 반환
    @Override
    public Long countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(final Store store, final TimeRange timeRange, final DayOfWeek dayOfWeek) {
        return storeVisitCertRepository.countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(store, timeRange, dayOfWeek);
    }

    // 24시간 이내 방문인증 내역 존재 여부
    @Override
    public Boolean checkStoreVisitCertBy24Hours(final Store store, final Member member) {
        return storeVisitCertRepository.existsByStoreAndMemberWithin24Hours(store, member, LocalDateTime.now().minusHours(24));
    }

    // 사용자의 방문 인증 성공 횟수 반환
    @Override
    public Long getStoreVisitCertSuccessCount(final Member member) {
        return storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsTrue(member);
    }

    // 사용자의 방문 인증 실패 횟수 반환
    @Override
    public Long getStoreVisitCertFailCount(final Member member) {
        return storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsFalse(member);
    }

}
