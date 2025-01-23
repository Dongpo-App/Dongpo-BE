package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.common.dto.location.CoordinateRange;
import com.dongyang.dongpo.common.dto.location.LatLong;
import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.common.util.location.LocationUtil;
import com.dongyang.dongpo.common.util.member.MemberUtil;
import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.store.dto.*;
import com.dongyang.dongpo.domain.store.entity.*;
import com.dongyang.dongpo.domain.store.enums.OpenTime;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import com.dongyang.dongpo.domain.store.repository.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreVisitCertRepository storeVisitCertRepository;
    private final TitleService titleService;
    private final OpenPossibilityService openPossibilityService;
    private final BookmarkService bookmarkService;
    private final StoreReviewService storeReviewService;
    private final LocationUtil locationUtil;
    private final StoreUtil storeUtil;
    private final MemberUtil memberUtil;

    public Store findById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    public void addStore(StoreRegisterDto registerDto, Member member) {
        // 사용자의 현재 위치와 점포 등록 위치가 범위 내에 있는지 검증
        if (!locationUtil.verifyStoreRegistration(registerDto))
            throw new CustomException(ErrorCode.DISTANCE_OUT_OF_RANGE);

        Store store = storeRepository.save(registerDto.toEntity(member));

        store.addPayMethods(registerDto.getPayMethods());
        store.addOperatingDays(registerDto.getOperatingDays());
        log.info("member {} add store: {}", member.getId(), store.getId());

        Long count = storeRepository.countByMember(member);
        if (count.equals(3L))
            titleService.addTitle(member, Title.REGISTER_PRO);
    }

    public List<StoreDto> findAll() {
        List<Store> stores = storeRepository.findAll();
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }

    // 현재 위치 기준 주변 점포 조회 (북마크 여부 포함)
    public List<NearbyStoresResponseDto> findStoresByCurrentLocation(final LatLong latLong, final Member member) {
        CoordinateRange coordinateRange = locationUtil.calcCoordinateRangeByCurrentLocation(latLong);

        return storeRepository.findStoresWithBookmarksWithinRange(
                        coordinateRange.getMinLat(), coordinateRange.getMaxLat(),
                        coordinateRange.getMinLong(), coordinateRange.getMaxLong(),
                        member.getId())
                .stream()
                .map(result -> {
                    Store store = (Store) result[0];
                    boolean isBookmarked = result[1] != null;
                    return store.toNearbyStoresResponse(isBookmarked);
                }).toList();
    }

    // 점포 간략 정보 조회 (오픈 가능성, 북마크 여부, 최근 리뷰 사진 5개 반환)
    public StoreBasicInfoResponseDto getStoreBasicInfo(final Long id, final Member member) {
        Store store = findById(id);
        return store.toBasicInfoResponse(
                openPossibilityService.getOpenPossibility(store),
                bookmarkService.isStoreBookmarkedByMember(store, member),
                storeReviewService.getLatestReviewPicsByStoreId(id));
    }

    // 점포 상세 정보 조회
    public StoreDetailInfoResponseDto getStoreDetailInfo(final Long id, final Member member) {
        Store store = findById(id);

//        List<Member> mostVisitMembers = storeVisitCertRepository.findTopVisitorsByStore(store);
        //        response.setMostVisitMembers(mostVisitMembers.stream()
//                .map(m -> MostVisitMemberResponse.of(m.getId(), m.getNickname(), m.getMainTitle(), m.getProfilePic()))
//                .toList());

        return store.toDetailInfoResponse(
                openPossibilityService.getOpenPossibility(store),
                bookmarkService.isStoreBookmarkedByMember(store, member),
                bookmarkService.getBookmarkCountByStore(store),
                storeReviewService.getLatestReviewPicsByStoreId(id)
        );
    }

    public void deleteStore(final Long id, final Member member) {
        Store store = findById(id);
        if (!store.getMember().getId().equals(member.getId()))
            throw new CustomException(ErrorCode.RESOURCE_NOT_OWNED_BY_USER);

        store.updateStoreStatusDeleted();
        log.info("Deleted Store: {} by Member: {}", store.getId(), member.getEmail());
    }

    // 점포 정보 수정
    public void updateStoreInfo(final Long id, final StoreInfoUpdateDto updateDto, final Member member) {
        Store store = findById(id);

        // 사용자가 등록한 점포인지 확인
        if (!store.getMember().getId().equals(member.getId()))
            throw new CustomException(ErrorCode.RESOURCE_NOT_OWNED_BY_USER);

        // 정보 업데이트
        store.updateInfo(updateDto);
        log.info("Member {} updated store: {}", member.getId(), store.getId());
    }

    public List<MyRegisteredStoresResponseDto> getMyRegisteredStores(Member member) {
        return storeRepository.findByMember(member).stream().map(Store::toMyRegisteredStoresResponse).toList();
    }

    public Long getMyRegisteredStoreCount(Member member) {
        return storeRepository.countByMember(member);
    }

    public StoreDto findOne(Long id) {
        return findById(id).toResponse();
    }

    public RecommendResponse recommendStoreByAge(Member member) {
        Pageable pageable = PageRequest.of(0, 3);
        String ageGroup = memberUtil.getAgeGroup(member.getBirthyear());
        List<Store> stores = storeRepository.findStoresByMemberAgeWithMostVisits(Integer.parseInt(ageGroup), pageable);

        return RecommendResponse.fromAge(stores, ageGroup);
    }

    public RecommendResponse recommendStoreByGender(Member member) {
        Pageable pageable = PageRequest.of(0, 3);
        List<Store> stores = storeRepository.findStoresByMemberGenderWithMostVisits(member.getGender(), pageable);

        return RecommendResponse.fromGender(stores, member.getGender());
    }

    // 비교 대상 점포의 좌표 반환
    private LatLong getStoreCoordinates(final Long storeId) {
        Store targetStore = findById(storeId);
        return LatLong.builder()
                .latitude(targetStore.getLatitude())
                .longitude(targetStore.getLongitude())
                .build();
    }

    // 신규 좌표가 기존 좌표의 오차범위 내에 위치하는지 검증 (방문 인증 검증)
    public void visitCert(final Long storeId, final StoreVisitCertDto storeVisitCertDto, final Member member) {
        final LatLong requestCoordinate = LatLong.builder()
                .latitude(storeVisitCertDto.getLatitude())
                .longitude(storeVisitCertDto.getLongitude())
                .build();

        // 점포 조회 (점포가 존재하지 않을 경우 예외 발생)
        final Store store = findById(storeId);

        // 24시간 이내 해당 점포에 방문 인증한 기록이 있을 경우 예외 발생
        if (checkVisitCertBy24Hours(storeId, member))
            throw new CustomException(ErrorCode.STORE_VISIT_CERT_NOT_AVAILABLE);

        // 방문 인증 거리 검증 (100m 초과 시 예외 발생)
        if (locationUtil.calcDistance(requestCoordinate, getStoreCoordinates(store.getId())) >= 100)
            throw new CustomException(ErrorCode.DISTANCE_OUT_OF_RANGE);

        final LocalDateTime now = LocalDateTime.now();
        final OpenTime openTime = storeUtil.getOpenTime(now);
        if (storeVisitCertDto.getIsVisitSuccessful()) { // 방문 인증 성공
            storeVisitCertRepository.save(StoreVisitCert.builder()
                    .store(store)
                    .member(member)
                    .isVisitSuccessful(true)
                    .certDate(now)
                    .certDay(now.getDayOfWeek())
                    .certTimeRange(openTime)
                    .build());

            final Long successCount = storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsTrue(member);
            final Long firstStoreVisitCount = storeVisitCertRepository.countByStoreAndIsVisitSuccessfulTrue(store);
            if (firstStoreVisitCount.equals(1L))
                titleService.addTitle(member, Title.FIRST_VISIT_CERT);
            else if (successCount.equals(3L))
                titleService.addTitle(member, Title.REGULAR_CUSTOMER);

        } else {
            storeVisitCertRepository.save(StoreVisitCert.builder()
                    .store(store)
                    .member(member)
                    .isVisitSuccessful(false)
                    .certDate(now)
                    .certDay(now.getDayOfWeek())
                    .certTimeRange(openTime)
                    .build());

            final Long failCount = storeVisitCertRepository.countByMemberAndIsVisitSuccessfulIsFalse(member);
            if (failCount.equals(3L))
                titleService.addTitle(member, Title.FAILED_TO_VISIT);
        }
    }

    public Boolean checkVisitCertBy24Hours(Long storeId, Member member) {
        Store store = findById(storeId);

        StoreVisitCert storeVisitCert = storeVisitCertRepository
                .findTopByStoreAndMemberOrderByCertDateDesc(store, member)
                .orElse(null);

        if (storeVisitCert == null)
            return false;

        return storeVisitCert.is24HoursCheck();
    }
}
