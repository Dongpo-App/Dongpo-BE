package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.store.AddStoreRequest;
import com.dongyang.dongpo.dto.store.StoreResponse;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public ResponseEntity addStore(AddStoreRequest request, String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Store store = request.toStore(member);
        storeRepository.save(store);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity allStore() {
        List<Store> stores = storeRepository.findAll();
        List<StoreResponse> storeResponses = new ArrayList<>();

        for (Store store : stores)
            storeResponses.add(store.toResponse());

        return ResponseEntity.ok().body(storeResponses);
    }
}
