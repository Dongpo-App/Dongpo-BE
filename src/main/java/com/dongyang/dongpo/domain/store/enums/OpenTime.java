package com.dongyang.dongpo.domain.store.enums;

import lombok.Getter;

@Getter
public enum OpenTime {

    MIDNIGHT_TO_3AM("00:00 - 03:00"),
    THREE_AM_TO_6AM("03:00 - 06:00"),
    SIX_AM_TO_9AM("06:00 - 09:00"),
    NINE_AM_TO_NOON("09:00 - 12:00"),
    NOON_TO_3PM("12:00 - 15:00"),
    THREE_PM_TO_6PM("15:00 - 18:00"),
    SIX_PM_TO_9PM("18:00 - 21:00"),
    NINE_PM_TO_MIDNIGHT("21:00 - 00:00");

    private final String timeRange;

    OpenTime(String timeRange) {
        this.timeRange = timeRange;
    }

}
