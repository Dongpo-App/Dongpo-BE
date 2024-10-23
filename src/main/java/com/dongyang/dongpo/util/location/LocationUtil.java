package com.dongyang.dongpo.util.location;

import com.dongyang.dongpo.dto.location.CoordinateRange;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationUtil {

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

    // 현재 위치를 기준으로 1km 이내의 좌표 범위 계산
    public CoordinateRange calcCoordinateRangeByCurrentLocation(LatLong latLong) {
        double distanceInKm = 1; // 1km 이내의 점포 검색

        // 위도와 경도의 차이 계산
        double deltaLat = distanceInKm / 111;
        double deltaLong = distanceInKm / (111 * Math.cos(Math.toRadians(latLong.getLatitude())));

        return CoordinateRange.builder()
                .minLat(latLong.getLatitude() - deltaLat)
                .maxLat(latLong.getLatitude() + deltaLat)
                .minLong(latLong.getLongitude() - deltaLong)
                .maxLong(latLong.getLongitude() + deltaLong)
                .build();
    }

    // 점포 등록 시 좌표 검증
    public Boolean verifyStoreRegistration(StoreRegisterDto request) {
        // 등록 하고자 하는 점포의 좌표
        LatLong storeCord =  LatLong.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        // 사용자의 현재 좌표
        LatLong userCord =  LatLong.builder()
                .latitude(request.getCurrentLatitude())
                .longitude(request.getCurrentLongitude())
                .build();

        // 오차가 100m 이내일 경우 true, 초과일 경우 false 반환
        return calcDistance(userCord, storeCord) <= 100;
    }
}
