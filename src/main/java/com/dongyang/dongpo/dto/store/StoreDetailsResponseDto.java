package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDetailsResponseDto {
    private Long id;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isToiletValid;
    private List<Store.OperatingDay> operatingDays;
    private List<Store.PayMethod> payMethods;
    private Long visitSuccessfulCount;
    private Long visitFailCount;
    private Long bookmarkCount;
}
