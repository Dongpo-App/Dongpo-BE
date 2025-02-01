package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.TimeRange;

import java.time.DayOfWeek;

public interface StoreVisitCertService {
    void addStoreVisitCert(final Store store, final Member member, final Boolean isVisitSuccessful);

    Long countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(final Store store, final TimeRange timeRange, final DayOfWeek dayOfWeek);

    Boolean checkStoreVisitCertBy24Hours(final Store store, final Member member);

    Long getStoreVisitCertSuccessCount(final Member member);

    Long getStoreVisitCertFailCount(final Member member);
}
