package com.dongyang.dongpo.service.location;

import com.dongyang.dongpo.dto.location.CoordinateDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocationService {

    // 두 개의 좌표를 비교하여 직선 거리 계산
    public Long calcDistance(CoordinateDto newCrd, CoordinateDto targetCrd) {
        final double EARTH_RADIUS = 6371.01; // 지구 반지름 (킬로미터)
        double latDistance = Math.toRadians(newCrd.getLat() - targetCrd.getLat());
        double lngDistance = Math.toRadians(newCrd.getLng() - targetCrd.getLng());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(targetCrd.getLat())) * Math.cos(Math.toRadians(newCrd.getLat()))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return Math.round(EARTH_RADIUS * c * 1000); // 결과값을 미터 단위로 반환
    }

}
