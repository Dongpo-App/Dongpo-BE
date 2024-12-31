package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
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
public class StoreRegisterDto {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean isToiletValid;
    private List<Store.OperatingDay> operatingDays;
    private List<Store.PayMethod> payMethods;

    // 등록하는 사용자의 현재 좌표
    private double currentLatitude;
    private double currentLongitude;

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
                .build();
    }
}
