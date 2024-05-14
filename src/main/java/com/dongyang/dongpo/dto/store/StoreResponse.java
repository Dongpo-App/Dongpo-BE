package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.store.OperatingDay;
import com.dongyang.dongpo.domain.store.PayMethod;
import com.dongyang.dongpo.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreResponse {

    private String name;
    private String location;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long memberId;
    private boolean isToiletValid;
    private Store.StoreStatus status;
    private List<OperatingDay> operatingDays;
    private List<PayMethod> payMethods;

}
