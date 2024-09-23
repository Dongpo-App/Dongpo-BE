package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import com.dongyang.dongpo.dto.location.CoordinateRange;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import com.dongyang.dongpo.dto.store.StoreUpdateDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.store.StoreOperatingDayRepository;
import com.dongyang.dongpo.repository.store.StorePayMethodRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    }

    public List<StoreDto> findAll() {
        List<Store> stores = storeRepository.findAll();
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }

    public List<StoreDto> findStoresByCurrentLocation(LatLong latLong) {
        CoordinateRange coordinateRange = locationService.calcCoordinateRangeByCurrentLocation(latLong);

        List<StoreDto> stores = new ArrayList<>();
        for (Store store : storeRepository.findStoresWithinRange(coordinateRange.getMinLat(), coordinateRange.getMaxLat(),
                                                                 coordinateRange.getMinLong(), coordinateRange.getMaxLong())
            ) stores.add(store.toResponse());
        return stores;
    }

    public StoreDto detailStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        return store.toResponse();
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
                storePayMethodRepository.deleteAll(existingPayMethods);
                for (Store.PayMethod payMethod : request.getPayMethods()) {
                    StorePayMethod newPayMethod = StorePayMethod.builder()
                            .store(store)
                            .payMethod(payMethod)
                            .build();
                    storePayMethodRepository.save(newPayMethod);
                }
            }
            if (request.getOperatingDays() != null && !request.getOperatingDays().isEmpty()) {
                List<StoreOperatingDay> existingOperatingDays = storeOperatingDayRepository.findByStore(store);
                storeOperatingDayRepository.deleteAll(existingOperatingDays);
                for (Store.OperatingDay operatingDay : request.getOperatingDays()) {
                    StoreOperatingDay newOperatingDay = StoreOperatingDay.builder()
                            .store(store)
                            .operatingDay(operatingDay)
                            .build();
                    storeOperatingDayRepository.save(newOperatingDay);
                }
            }
            log.info("member {} update store: {}",member.getId(), store.getId());
        } else throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);
    }

    public List<StoreDto> myRegStore(Member member){
        List<Store> stores = storeRepository.findByMember(member);
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }

    public StoreDto findOne(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return store.toResponse();
    }
}
