package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import com.dongyang.dongpo.domain.store.StorePayMethod;
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
    private List<StoreOperatingDay.OperatingDay> operatingDays;
    private List<StorePayMethod.PayMethod> payMethods;

    public Store toStore(Member member){
//        List<StoreOperatingDay> storeOperatingDays = operatingDays.stream().map(day ->
//                StoreOperatingDay.builder()
//                        .operatingDay(day)
//                        .build()
//        ).toList();
//
//        List<StorePayMethod> storePayMethods = payMethods.stream().map(payMethod ->
//                StorePayMethod.builder()
//                        .payMethod(payMethod)
//                        .build()
//        ).toList();

        return Store.builder()
                .name(name)
                .location(location)
                .openTime(openTime)
                .closeTime(closeTime)
                .member(member)
                .isToiletValid(isToiletValid)
                .build();
    }
}
