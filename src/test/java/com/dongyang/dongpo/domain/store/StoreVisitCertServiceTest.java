package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreVisitCert;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    private StoreVisitCertService storeVisitCertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Subclass to access protected methods
    private class StoreVisitCertServiceTestHelper extends StoreVisitCertService {
        StoreVisitCertServiceTestHelper() {
            super(storeVisitCertRepository, storeUtil);
        }

        @Override
        protected void addStoreVisitCert(Store store, Member member, Boolean isVisitSuccessful) {
            super.addStoreVisitCert(store, member, isVisitSuccessful);
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
        protected Long getStoreVisitCertFailCount(Member member) {
            return super.getStoreVisitCertFailCount(member);
        }
    }

    @Test
    void addStoreVisitCert() {
        Store store = mock(Store.class);
        Member member = mock(Member.class);
        Boolean isVisitSuccessful = true;

        StoreVisitCertServiceTestHelper helper = new StoreVisitCertServiceTestHelper();
        helper.addStoreVisitCert(store, member, isVisitSuccessful);

        verify(storeVisitCertRepository).save(any(StoreVisitCert.class));
    }

    @Test
    void checkStoreVisitCertBy24Hours() {
        Store store = mock(Store.class);
        Member member = mock(Member.class);

        when(storeVisitCertRepository.existsByStoreAndMemberWithin24Hours(any(), any(), any())).thenReturn(true);

        StoreVisitCertServiceTestHelper helper = new StoreVisitCertServiceTestHelper();
        Boolean result = helper.checkStoreVisitCertBy24Hours(store, member);

        assertTrue(result);
        verify(storeVisitCertRepository).existsByStoreAndMemberWithin24Hours(any(), any(), any());
    }

    @Test
    void getStoreVisitCertSuccessCount() {
        Member member = mock(Member.class);

        when(storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsTrue(any())).thenReturn(5L);

        StoreVisitCertServiceTestHelper helper = new StoreVisitCertServiceTestHelper();
        Long successCount = helper.getStoreVisitCertSuccessCount(member);

        assertTrue(successCount == 5L);
        verify(storeVisitCertRepository).countByMemberAndIsVisitSuccessfulIsTrue(any());
    }

    @Test
    void getStoreVisitCertFailCount() {
        Member member = mock(Member.class);

        when(storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsFalse(any())).thenReturn(3L);

        StoreVisitCertServiceTestHelper helper = new StoreVisitCertServiceTestHelper();
        Long failCount = helper.getStoreVisitCertFailCount(member);

        assertTrue(failCount == 3L);
        verify(storeVisitCertRepository).countByMemberAndIsVisitSuccessfulIsFalse(any());
    }
}