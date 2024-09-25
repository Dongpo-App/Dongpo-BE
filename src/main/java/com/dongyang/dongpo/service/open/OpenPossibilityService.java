package com.dongyang.dongpo.service.open;

import com.dongyang.dongpo.domain.store.OpenTime;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.store.OpenPossibility;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenPossibilityService {

    private final StoreVisitCertRepository storeVisitCertRepository;

    public OpenPossibility getOpenPossibility(Store store) {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek(); // 현재 요일
        OpenTime openTime = getOpenTime(now); // 현재 시간대

        // 해당 점포 방문인증 성공한 데이터만 조회
        List<OpenPossibility> possibilities = storeVisitCertRepository.findByStoreAndAndIsVisitSuccessfulTrue(store)
                .stream()
                .map(s -> {
                    if (s.getOpenTime().equals(openTime) && s.getOpenDay().equals(dayOfWeek))
                        return OpenPossibility.HIGH; // 성공한 경우
                    else
                        return OpenPossibility.NONE; // 실패한 경우
                })
                .toList(); // 결과를 리스트로 수집

        if (possibilities.contains(OpenPossibility.HIGH))
            return OpenPossibility.HIGH; // 하나라도 HIGH가 있으면 HIGH 반환
        else
            return OpenPossibility.NONE; // 모두 NONE이면 NONE 반환

    }

    private OpenTime getOpenTime(LocalDateTime now){
        int hour = now.getHour();
        if (hour < 3)
            return OpenTime.MIDNIGHT_TO_3AM;
        else if (hour < 6)
            return OpenTime.THREE_AM_TO_6AM;
        else if (hour < 9)
            return OpenTime.SIX_AM_TO_9AM;
        else if (hour < 12)
            return OpenTime.NINE_AM_TO_NOON;
        else if (hour < 15)
            return OpenTime.NOON_TO_3PM;
        else if (hour < 18)
            return OpenTime.THREE_PM_TO_6PM;
        else if (hour < 21)
            return OpenTime.SIX_PM_TO_9PM;
        else
            return OpenTime.NINE_PM_TO_MIDNIGHT;
    }
}
