package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import com.dongyang.dongpo.repository.store.StoreOperatingDayRepository;
import com.dongyang.dongpo.repository.store.StorePayMethodRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.service.location.LocationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StorePayMethodRepository storePayMethodRepository;

    @Mock
    private StoreOperatingDayRepository storeOperatingDayRepository;

    @InjectMocks
    private StoreService storeService;

    @Mock
    private LocationService locationService;

    @Test
    @DisplayName("점포_등록")
    void addStore() {
        // given
        StoreRegisterDto storeDto = mock(StoreRegisterDto.class);
        Member member = mock(Member.class);
        Store store = mock(Store.class);
        StorePayMethod storePayMethod = mock(StorePayMethod.class);
        StoreOperatingDay storeOperatingDay = mock(StoreOperatingDay.class);

        when(locationService.verifyStoreRegistration(storeDto)).thenReturn(true);
        when(storeRepository.save(any())).thenReturn(store);
        when(storePayMethodRepository.save(any())).thenReturn(storePayMethod);
        when(storeOperatingDayRepository.save(any())).thenReturn(storeOperatingDay);
        when(storeDto.getPayMethods()).thenReturn(List.of(Store.PayMethod.CASH, Store.PayMethod.CARD));
        when(storeDto.getOperatingDays()).thenReturn(List.of(Store.OperatingDay.MON, Store.OperatingDay.TUE));

        // when
        storeService.addStore(storeDto, member);

        // then
        verify(storeRepository).save(any());
        verify(storePayMethodRepository, times(2)).save(any());
        verify(storeOperatingDayRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("점포_목록_조회")
    void findAll() {
        when(storeRepository.findAll()).thenReturn(List.of(mock(Store.class)));
        List<StoreDto> storeDtos = storeService.findAll();

        assertFalse(storeDtos.isEmpty());
        verify(storeRepository).findAll();
    }

    @Test
    void findStoresByCurrentLocation() {
    }

    @Test
    void detailStore() {
        // given
        Store store = mock(Store.class);
        Optional<Store> optionalStore = Optional.of(store);

        when(storeRepository.findById(any())).thenReturn(optionalStore);

        // when
        storeService.detailStore(store.getId());

        // then
        verify(storeRepository).findById(any());
    }

    @Test
    void deleteStore() {
    }

    @Test
    void updateStore() {
    }

    @Test
    void myRegStore() {
    }

    @Test
    void findOne() {
    }
}