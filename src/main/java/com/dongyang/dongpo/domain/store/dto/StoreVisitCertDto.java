package com.dongyang.dongpo.domain.store.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreVisitCertDto {
    private double latitude; // 위도
    private double longitude; // 경도
    private Long storeId;
    private boolean isVisitSuccessful;
}
