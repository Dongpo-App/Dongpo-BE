package com.dongyang.dongpo.util.store;

import com.dongyang.dongpo.domain.store.OpenTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class StoreUtil {

    // 오픈 시간대 반환
    public OpenTime getOpenTime(LocalDateTime now){
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
