package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.common.dto.location.LatLong;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreService {
    void addStore(StoreRegisterDto registerDto, Member member);

    List<StoreDto> findAll();

    List<NearbyStoresResponseDto> findStoresByCurrentLocation(LatLong latLong, Member member);

    StoreBasicInfoResponseDto getStoreBasicInfo(Long id, Member member);

    StoreDetailInfoResponseDto getStoreDetailInfo(Long id, Member member);

    void deleteStore(Long id, Member member);

    void updateStoreInfo(Long id, StoreInfoUpdateDto storeInfoUpdateDto, Member member);

    Page<MyRegisteredStoresResponseDto> getMyRegisteredStores(Member member, int page);

    Long getMyRegisteredStoreCount(Member member);

    StoreDto findOne(Long id);

    RecommendResponse recommendStoreByAge(Member member);

    RecommendResponse recommendStoreByGender(Member member);

    void visitCert(Long storeId, StoreVisitCertDto storeVisitCertDto, Member member);

    Boolean checkVisitCertBy24Hours(Long storeId, Member member);
}
