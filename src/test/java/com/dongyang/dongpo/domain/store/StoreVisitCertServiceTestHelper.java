package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.TimeRange;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertService;

import java.time.DayOfWeek;

public class StoreVisitCertServiceTestHelper extends StoreVisitCertService {
    public StoreVisitCertServiceTestHelper(StoreVisitCertRepository storeVisitCertRepository, StoreUtil storeUtil) {
        super(storeVisitCertRepository, storeUtil);
    }

    @Override
    protected Long countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(Store store, TimeRange timeRange, DayOfWeek dayOfWeek) {
        return super.countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(store, timeRange, dayOfWeek);
    }

    @Override
    protected Boolean checkStoreVisitCertBy24Hours(Store store, Member member) {
        return super.checkStoreVisitCertBy24Hours(store, member);
    }

    @Override
    protected Long getStoreVisitCertSuccessCount(Member member) {
        return super.getStoreVisitCertSuccessCount(member);
    }

    @Override
    protected void addStoreVisitCert(Store store, Member member, Boolean isVisitSuccessful) {
        super.addStoreVisitCert(store, member, isVisitSuccessful);
    }
}
