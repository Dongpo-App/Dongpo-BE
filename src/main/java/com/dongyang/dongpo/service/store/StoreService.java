package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public ResponseEntity addStore(StoreDto request, String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Store store = request.toStore(member);
        storeRepository.save(store);

        log.info("회원 {}가 점포 {}을 등록했습니다.", member.getId(), store.getId());
        return ResponseEntity.ok().build();
    }

    public List<StoreDto> allStore() {
        List<Store> stores = storeRepository.findAll();
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }

    public ResponseEntity detailStore(Long id) throws Exception {
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        List<ReviewDto> reviewDtos = new ArrayList<>();

        if (store.getReviews() != null) {
            for (StoreReview review : store.getReviews())
                reviewDtos.add(review.toResponse());
        }


        return ResponseEntity.ok().body(store.toResponse(reviewDtos));
    }

    @Transactional
    public ResponseEntity deleteStore(Long id) {
        storeRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity updateStore(Long id, StoreDto request) throws Exception{
        Store store = storeRepository.findById(id).orElseThrow(StoreNotFoundException::new);
        store.update(request);
        storeRepository.save(store);

        return ResponseEntity.ok().build();
    }

    public List<StoreDto> myRegStore(String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        List<Store> stores = storeRepository.findByMemberId(member.getId());
        List<StoreDto> storeResponse = new ArrayList<>();

        for (Store store : stores)
            storeResponse.add(store.toResponse());

        return storeResponse;
    }
}
