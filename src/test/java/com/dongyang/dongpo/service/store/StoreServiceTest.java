package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.repository.store.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    @DisplayName("점포_등록")
    void addStore() {
        StoreDto storeDto = mock(StoreDto.class);
        Member member = mock(Member.class);

        when(storeRepository.save(any())).thenReturn(mock(Store.class));

        storeService.addStore(storeDto, member);

        verify(storeRepository).save(any());
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