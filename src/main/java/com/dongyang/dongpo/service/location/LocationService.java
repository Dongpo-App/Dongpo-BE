package com.dongyang.dongpo.service.location;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.location.LatLongComparisonDto;
import com.dongyang.dongpo.dto.location.CoordinateRange;
import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final StoreRepository storeRepository;
    private final StoreVisitCertRepository storeVisitCertRepository;
    private final MemberRepository memberRepository;


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
        Store targetStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

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

    // 신규 좌표가 기존 좌표의 오차범위 내에 위치하는지 검증 (방문 인증 검증)
    public Boolean verifyVisitCert(LatLongComparisonDto latLongComparison, Member member) {
        LatLong newCoordinate = LatLong.builder()
                .latitude(latLongComparison.getLatitude())
                .longitude(latLongComparison.getLongitude())
                .build();

        boolean verify = calcDistance(newCoordinate, getStoreCoordinates(latLongComparison.getTargetStoreId())) <= 50;
        if (verify){
            storeVisitCertRepository.save(StoreVisitCert.builder()
                    .store(storeRepository.findById(latLongComparison.getTargetStoreId()).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND)))
                    .member(member)
                    .isVisitSuccessful(true)
                    .certDate(LocalDateTime.now())
                    .build());

            Long count = storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsTrue(member);
            if (count == 1 && member.getTitles().stream().noneMatch(title -> title.getTitle().equals(Title.FIRST_VISIT_CERT))) {
                member.addTitle(MemberTitle.builder()
                        .title(Title.FIRST_VISIT_CERT)
                        .achieveDate(LocalDateTime.now())
                        .member(member)
                        .build());
                memberRepository.save(member);

                log.info("member {} add title : {}", member.getId(), Title.FIRST_VISIT_CERT.getDescription());
            }
        }else {
            storeVisitCertRepository.save(StoreVisitCert.builder()
                    .store(storeRepository.findById(latLongComparison.getTargetStoreId()).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND)))
                    .member(member)
                    .isVisitSuccessful(false)
                    .certDate(LocalDateTime.now())
                    .build());
        }


        // 오차가 50m 이내일 경우 true, 초과일 경우 false 반환
        return verify;
    }

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
