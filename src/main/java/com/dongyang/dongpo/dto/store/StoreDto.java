package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
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
public class StoreDto {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Long memberId;
    private int reportCount;
    private boolean isToiletValid;
    private Store.StoreStatus status;
    private List<Store.OperatingDay> operatingDays;
    private List<Store.PayMethod> payMethods;
    private List<ReviewDto> reviews;

    public Store toStore(Member member){
        return Store.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .openTime(openTime)
                .closeTime(closeTime)
                .member(member)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDays)
                .payMethods(payMethods)
                .build();
    }

}
