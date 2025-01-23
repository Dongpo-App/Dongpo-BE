package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.TimeRange;
import com.dongyang.dongpo.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OpenPossibilityService {

    private final StoreVisitCertService storeVisitCertService;
    private final StoreUtil storeUtil;

    protected OpenPossibility getOpenPossibility(Store store) {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek(); // 현재 요일
        TimeRange timeRange = storeUtil.getTimeRange(now.getHour()); // 현재 시간대

        // 현재 시간대, 요일에 방문 인증 성공한 횟수
        Long successCount = storeVisitCertService.countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(store, timeRange, dayOfWeek);

        // 횟수에 따라 OpenPossibility 결정
        return switch (successCount.intValue()) {
            case 0 -> OpenPossibility.NONE;
            case 1, 2, 3 -> OpenPossibility.LOW;
            case 4, 5, 6, 7 -> OpenPossibility.MID;
            case 8, 9, 10 -> OpenPossibility.HIGH;
            default -> OpenPossibility.VERY_HIGH;
        };
    }

}
