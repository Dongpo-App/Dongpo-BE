package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.OperatingDay;
import com.dongyang.dongpo.domain.store.PayMethod;
import com.dongyang.dongpo.domain.store.Store;
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
public class AddStoreRequest {

    private String name;
    private String location;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isToiletValid;
    private List<OperatingDay> operatingDays;
    private List<PayMethod> payMethods;

    public Store toStore(Member member){
        return Store.builder()
                .name(name)
                .location(location)
                .openTime(openTime)
                .closeTime(closeTime)
                .member(member)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDays)
                .payMethods(payMethods)
                .build();
    }
}
