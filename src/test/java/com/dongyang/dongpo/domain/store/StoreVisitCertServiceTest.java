package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreVisitCert;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreVisitCertServiceTest {

    @Mock
    private StoreVisitCertRepository storeVisitCertRepository;

    @Mock
    private StoreUtil storeUtil;

    @InjectMocks
    private StoreVisitCertServiceImpl storeVisitCertService;

    @Test
    void addStoreVisitCert() {
        Store store = mock(Store.class);
        Member member = mock(Member.class);
        Boolean isVisitSuccessful = true;

        storeVisitCertService.addStoreVisitCert(store, member, isVisitSuccessful);

        verify(storeVisitCertRepository).save(any(StoreVisitCert.class));
    }

    @Test
    void checkStoreVisitCertBy24Hours() {
        Store store = mock(Store.class);
        Member member = mock(Member.class);

        when(storeVisitCertRepository.existsByStoreAndMemberWithin24Hours(any(), any(), any())).thenReturn(true);

        Boolean result = storeVisitCertService.checkStoreVisitCertBy24Hours(store, member);

        assertTrue(result);
        verify(storeVisitCertRepository).existsByStoreAndMemberWithin24Hours(any(), any(), any());
    }

    @Test
    void getStoreVisitCertSuccessCount() {
        Member member = mock(Member.class);

        when(storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsTrue(any())).thenReturn(5L);

        Long successCount = storeVisitCertService.getStoreVisitCertSuccessCount(member);

        assertTrue(successCount == 5L);
        verify(storeVisitCertRepository).countByMemberAndIsVisitSuccessfulIsTrue(any());
    }

    @Test
    void getStoreVisitCertFailCount() {
        Member member = mock(Member.class);

        when(storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsFalse(any())).thenReturn(3L);

        Long failCount = storeVisitCertService.getStoreVisitCertFailCount(member);

        assertTrue(failCount == 3L);
        verify(storeVisitCertRepository).countByMemberAndIsVisitSuccessfulIsFalse(any());
    }
}
