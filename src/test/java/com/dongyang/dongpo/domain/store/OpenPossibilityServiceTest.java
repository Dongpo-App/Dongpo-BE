package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.TimeRange;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import com.dongyang.dongpo.domain.store.service.OpenPossibilityServiceImpl;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenPossibilityServiceTest {

    @Mock
    private StoreUtil storeUtil;

    @Mock
    private StoreVisitCertRepository storeVisitCertRepository;

    @Mock
    private StoreVisitCertServiceImpl storeVisitCertService;

    @InjectMocks
    private OpenPossibilityServiceImpl openPossibilityService;

    @Test
    void getOpenPossibility() {
        Store store = mock(Store.class);
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        TimeRange timeRange = TimeRange.MIDNIGHT_TO_3AM;

        when(storeUtil.getTimeRange(any(Integer.class))).thenReturn(timeRange);
        when(storeVisitCertService.countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(any(Store.class), any(TimeRange.class), any(DayOfWeek.class))).thenReturn(5L);

        OpenPossibility result = openPossibilityService.getOpenPossibility(store);

        assertEquals(OpenPossibility.MID, result);
    }
}
