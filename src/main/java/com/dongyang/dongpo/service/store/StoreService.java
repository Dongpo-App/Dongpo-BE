package com.dongyang.dongpo.service.store;

import static java.util.stream.Collectors.*;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.location.CoordinateRange;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.*;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.store.StoreOperatingDayRepository;
import com.dongyang.dongpo.repository.store.StorePayMethodRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import com.dongyang.dongpo.service.location.LocationService;
import com.dongyang.dongpo.service.open.OpenPossibilityService;
import com.dongyang.dongpo.service.title.TitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.joda.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final StorePayMethodRepository storePayMethodRepository;
    private final StoreOperatingDayRepository storeOperatingDayRepository;
    private final LocationService locationService;
    private final TitleService titleService;
    private final OpenPossibilityService openPossibilityService;
    private final BookmarkService bookmarkService;
    private final StoreReviewService storeReviewService;


    @Transactional
    public void addStore(StoreRegisterDto registerDto, Member member) {
        // 사용자의 현재 위치와 점포 등록 위치가 범위 내에 있는지 검증
        if (!locationService.verifyStoreRegistration(registerDto))
            throw new CustomException(ErrorCode.STORE_REGISTRATION_NOT_VALID);

        Store savedStore = storeRepository.save(registerDto.toStore(member));

        for (Store.PayMethod payMethod : registerDto.getPayMethods()) {
            StorePayMethod storePayMethod = StorePayMethod.builder()
                    .store(savedStore)
                    .payMethod(payMethod)
                    .build();
            storePayMethodRepository.save(storePayMethod);
        }

        for (Store.OperatingDay operatingDay : registerDto.getOperatingDays()) {
            StoreOperatingDay storeOperatingDay = StoreOperatingDay.builder()
                    .store(savedStore)
                    .operatingDay(operatingDay)
                    .build();
            storeOperatingDayRepository.save(storeOperatingDay);
        }

        log.info("member {} add store: {}", member.getId(), savedStore.getId());

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

    public List<StoreIndexDto> findStoresByCurrentLocation(LatLong latLong, Member member) {
        CoordinateRange coordinateRange = locationService.calcCoordinateRangeByCurrentLocation(latLong);

        List<BookmarkDto> myBookmarks = bookmarkService.getMyBookmarks(member);

        return storeRepository.findStoresWithinRange(coordinateRange.getMinLat(), coordinateRange.getMaxLat(),
                                                     coordinateRange.getMinLong(), coordinateRange.getMaxLong())
                .stream()
                .map(store -> {
                    boolean isBookmarked = myBookmarks.stream()
                            .anyMatch(bookmark -> store.getId().equals(bookmark.getStoreId()));

                    return store.toIndexResponse(isBookmarked, openPossibilityService.getOpenPossibility(store));
                })
                .collect(toList());
    }

    public StoreIndexDto getStoreSummary(Long id, Member member) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return store.toIndexResponse(openPossibilityService.getOpenPossibility(store),
                                    bookmarkService.isStoreBookmarkedByMember(store, member),
                                    storeReviewService.getReviewPicsByStoreId(id));
    }

    public StoreDto detailStore(Long id, Member member) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return store.toResponse(openPossibilityService.getOpenPossibility(store),
                                bookmarkService.isStoreBookmarkedByMember(store, member));
    }

    @Transactional
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);

        log.info("delete store: {}", id); // 임시
    }

    @Transactional
    public void updateStore(Long id, StoreUpdateDto request, Member member) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 사용자가 등록한 점포인지 확인
        if (member.getEmail().equals(store.getMember().getEmail())) {
            store.update(request);
            storeRepository.save(store);

            if (request.getPayMethods() != null && !request.getPayMethods().isEmpty()) {
                List<StorePayMethod> existingPayMethods = storePayMethodRepository.findByStore(store);
                List<Store.PayMethod> newPayMethods = request.getPayMethods();

                // 기존 payMethod 목록에서 새로운 payMethod 목록에 없는 항목 삭제
                for (StorePayMethod existingPayMethod : existingPayMethods) {
                    if (!newPayMethods.contains(existingPayMethod.getPayMethod())) {
                        storePayMethodRepository.delete(existingPayMethod);
                    }
                }

                // 새로운 payMethod 목록에서 기존 payMethod 목록에 없는 항목 추가
                for (Store.PayMethod newPayMethod : newPayMethods) {
                    boolean exists = existingPayMethods.stream()
                            .anyMatch(existingPayMethod -> existingPayMethod.getPayMethod().equals(newPayMethod));
                    if (!exists) {
                        StorePayMethod storePayMethod = StorePayMethod.builder()
                                .store(store)
                                .payMethod(newPayMethod)
                                .build();
                        try {
                            storePayMethodRepository.save(storePayMethod);
                        } catch (DataIntegrityViolationException ignored) {
                            // 중복 예외 발생 시 무시
                        }
                    }
                }
            }

            if (request.getOperatingDays() != null && !request.getOperatingDays().isEmpty()) {
                List<StoreOperatingDay> existingOperatingDays = storeOperatingDayRepository.findByStore(store);
                List<Store.OperatingDay> newOperatingDays = request.getOperatingDays();

                // 기존 operatingDay 목록에서 새로운 operatingDay 목록에 없는 항목 삭제
                for (StoreOperatingDay existingOperatingDay : existingOperatingDays) {
                    if (!newOperatingDays.contains(existingOperatingDay.getOperatingDay())) {
                        storeOperatingDayRepository.delete(existingOperatingDay);
                    }
                }

                // 새로운 operatingDay 목록에서 기존 operatingDay 목록에 없는 항목 추가
                for (Store.OperatingDay newOperatingDay : newOperatingDays) {
                    boolean exists = existingOperatingDays.stream()
                            .anyMatch(existingOperatingDay -> existingOperatingDay.getOperatingDay().equals(newOperatingDay));
                    if (!exists) {
                        StoreOperatingDay storeOperatingDay = StoreOperatingDay.builder()
                                .store(store)
                                .operatingDay(newOperatingDay)
                                .build();
                        try {
                            storeOperatingDayRepository.save(storeOperatingDay);
                        } catch (DataIntegrityViolationException ignored) {
                            // 중복 예외 발생 시 무시
                        }
                    }
                }
            }

            log.info("member {} updated store: {}", member.getId(), store.getId());
        } else {
            throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);
        }
    }

    public List<StoreIndexDto> getMyRegisteredStores(Member member) {
        List<StoreIndexDto> storeIndexDtos = new ArrayList<>();
        storeRepository.findByMember(member).forEach(store -> storeIndexDtos.add(store.toIndexResponse()));
        return storeIndexDtos;
    }

    public Long getMyRegisteredStoreCount(Member member) {
        return storeRepository.countByMember(member);
    }

    public StoreDto findOne(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return store.toResponse();
    }

    public ApiResponse<List<RecommendResponse>> recommendStoreByAge(Member member) {
        Pageable pageable = PageRequest.of(0, 3);
        String ageGroup = getAgeGroup(member.getBirthyear());
        List<Store> stores = storeRepository.findStoresByMemberAgeWithMostVisits(Integer.parseInt(ageGroup),
			Integer.parseInt(member.getBirthyear()), pageable);

        List<RecommendResponse> list = stores.stream()
            .map(RecommendResponse::fromAge)
            .toList();

        return new ApiResponse<>(list, ageGroup);
    }

    public ApiResponse<List<RecommendResponse>> recommendStoreByGender(Member member) {
        Pageable pageable = PageRequest.of(0, 3);
        List<Store> stores = storeRepository.findStoresByMemberGenderWithMostVisits(member.getGender(), pageable);

        List<RecommendResponse> list = stores.stream()
            .map(RecommendResponse::fromGender)
            .toList();

        return new ApiResponse<>(list, member.getGender().toString());
    }

    private String getAgeGroup(String birthyear){
        int age = LocalDate.now().getYear() - Integer.parseInt(birthyear);
        if(age < 20) return "10";
        else if(age < 30) return "20";
        else if(age < 40) return "30";
        else if(age < 50) return "40";
        else if(age < 60) return "50";
        else return "60";
    }
}
