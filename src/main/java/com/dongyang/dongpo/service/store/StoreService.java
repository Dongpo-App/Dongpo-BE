package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import com.dongyang.dongpo.dto.store.AddStoreRequest;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.MemberRepository;
import com.dongyang.dongpo.repository.store.OperatingDayRepository;
import com.dongyang.dongpo.repository.store.PayMethodRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final OperatingDayRepository operatingDayRepository;
    private final PayMethodRepository payMethodRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public ResponseEntity addStore(AddStoreRequest request, String accessToken) throws Exception{
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        Store store = request.toStore(member);
        storeRepository.save(store);

        operatingDayRepository.saveAll(request.getOperatingDays().stream().map(day ->
                StoreOperatingDay.builder()
                        .store(store)
                        .operatingDay(day)
                        .build()
        ).toList());

        payMethodRepository.saveAll(request.getPayMethods().stream().map(payMethod ->
                StorePayMethod.builder()
                        .store(store)
                        .payMethod(payMethod)
                        .build()
        ).toList());


        return ResponseEntity.ok().build();
    }
}
