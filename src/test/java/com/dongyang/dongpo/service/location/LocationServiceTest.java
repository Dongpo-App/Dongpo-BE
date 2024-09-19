package com.dongyang.dongpo.service.location;

import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import com.dongyang.dongpo.repository.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void calcDistance() {
    }

    @Test
    void getDistance() {
    }

    @Test
    void verifyVisitCert() {
    }

    @Test
    void calcCoordinateRangeByCurrentLocation() {
    }

    @Test
    void verifyStoreRegistration() {
        // given
        StoreRegisterDto storeDto = StoreRegisterDto.builder()
                .name("테스트")
                .address("서울시 구로구")
                .latitude(37.49986174919337)
                .longitude(126.86771368999942)
                .currentLatitude(37.50289368559725)
                .currentLongitude(126.88044271559237)
                .build();

        // when
        Boolean result = locationService.verifyStoreRegistration(storeDto);

        // then
        assertFalse(result); // 100m 초과
    }

    @Test
    void verifyStoreRegistrationWithinRange() {
        // given
        StoreRegisterDto storeDto = StoreRegisterDto.builder()
                .name("테스트")
                .address("서울시 구로구")
                .latitude(37.50289368559725)
                .longitude(126.88044271559237)
                .currentLatitude(37.50289368559725)
                .currentLongitude(126.88044271559237)
                .build();

        // when
        Boolean result = locationService.verifyStoreRegistration(storeDto);

        // then
        assertTrue(result); // 100m 이내
    }
}