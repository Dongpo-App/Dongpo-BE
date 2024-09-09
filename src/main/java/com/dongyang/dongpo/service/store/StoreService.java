package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.location.CoordinateRange;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final LocationService locationService;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public void addStore(StoreDto request, String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Store store = request.toStore(member);
        storeRepository.save(store);

        log.info("member {} add store: {}", member.getId(), store.getId());
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

    public StoreDto detailStore(Long id) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        List<ReviewDto> reviewDtos = new ArrayList<>();

        if (store.getReviews() != null) {
            for (StoreReview review : store.getReviews())
                reviewDtos.add(review.toResponse());
        }

        return store.toResponse(reviewDtos);
    }

    @Transactional
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);

        log.info("delete store: {}", id); // 임시
    }

    @Transactional
    public void updateStore(Long id, StoreDto request, String token) throws Exception{
        String email = jwtTokenProvider.parseClaims(token).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);

        if (member == store.getMember()) {
            store.update(request);
            storeRepository.save(store);
        }else
            throw new MemberNotFoundException(); // 임시

        log.info("member {} update store: {}",member.getId(), store.getId());
    }

    public List<StoreDto> myRegStore(String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        List<Store> stores = storeRepository.findByMember(member);
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }

    public StoreDto findOne(Long id) throws StoreNotFoundException {
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        return store.toResponse();
    }
}
