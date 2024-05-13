package com.dongyang.dongpo.service.location;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.location.LatLongComparisonDto;
import com.dongyang.dongpo.exception.DataNotFoundException;
import com.dongyang.dongpo.repository.store.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocationService {

    private final StoreRepository storeRepository;


    @Autowired
    public LocationService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    // 두 개의 좌표를 비교하여 직선 거리 계산
    public Long calcDistance(LatLong newLatLong, LatLong targetLatLong) {
        final double EARTH_RADIUS = 6371.01; // 지구 반지름 (킬로미터)
        double latDistance = Math.toRadians(newLatLong.getLatitude() - targetLatLong.getLatitude());
        double lngDistance = Math.toRadians(newLatLong.getLongitude() - targetLatLong.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(targetLatLong.getLatitude())) * Math.cos(Math.toRadians(newLatLong.getLatitude()))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(EARTH_RADIUS * c * 1000); // 결과값을 미터 단위로 반환
    }

    // 비교 대상 점포의 좌표 반환
    private LatLong getStoreCoordinates(Long storeId) {
        Store targetStore = storeRepository.findById(storeId).orElseThrow(DataNotFoundException::new);
        return LatLong.builder()
                .latitude(targetStore.getLatitude())
                .longitude(targetStore.getLongitude())
                .build();
    }

    // 입력 된 좌표와 비교 대상 점포 좌표와의 직선 거리 반환
    public Long getDistance(LatLongComparisonDto latLongComparison) {
        LatLong newCoordinate = LatLong.builder()
                .latitude(latLongComparison.getLatitude())
                .longitude(latLongComparison.getLongitude())
                .build();

        // 두 좌표 간 직선 거리 계산 후 반환
        return calcDistance(newCoordinate, getStoreCoordinates(latLongComparison.getTargetStoreId()));
    }

    // 신규 좌표가 기존 좌표의 오차범위 내에 위치하는지 검증
    public Boolean verifyCoordinate(LatLongComparisonDto latLongComparison) {
        LatLong newCoordinate = LatLong.builder()
                .latitude(latLongComparison.getLatitude())
                .longitude(latLongComparison.getLongitude())
                .build();

        // 오차가 50m 이내일 경우 true, 초과일 경우 false 반환
        return calcDistance(newCoordinate, getStoreCoordinates(latLongComparison.getTargetStoreId())) <= 50;
    }

}
