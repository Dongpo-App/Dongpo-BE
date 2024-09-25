package com.dongyang.dongpo.service.open;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import com.dongyang.dongpo.dto.store.OpenPossibility;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenPossibilityService {

    private final StoreVisitCertRepository storeVisitCertRepository;

    public OpenPossibility getOpenPossibility(Store store) {
        return storeVisitCertRepository.findByStore(store)
                .map(storeVisitCert -> {
                    // 점포의 영업 시간과 요일을 확인하여 영업 가능 여부 판단
                    if (storeVisitCertRepository.existsByOpenDayAndOpenTimeAndIsVisitSuccessfulTrue(
                            storeVisitCert.getOpenDay(), storeVisitCert.getOpenTime())) {
                        return OpenPossibility.HIGH;
                    } else {
                        return OpenPossibility.NONE; // 성공 데이터 없는 경우
                    }
                })
                .orElse(OpenPossibility.NONE); // 방문 인증 데이터가 없는 경우
    }

}
