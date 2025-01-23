package com.dongyang.dongpo.common.util.store;

import com.dongyang.dongpo.domain.store.enums.TimeRange;
import org.springframework.stereotype.Component;

@Component
public class StoreUtil {

    // 시간대 반환
    public TimeRange getTimeRange(final int hour) {
        return TimeRange.values()[hour / 3];
    }
}
