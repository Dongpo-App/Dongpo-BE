package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import com.dongyang.dongpo.domain.store.enums.StoreStatus;
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
    private String memberNickname;
    private int reportCount;
    private Boolean isToiletValid;
    private StoreStatus status;
    private List<OperatingDay> operatingDays;
    private List<PayMethod> payMethods;
    private OpenPossibility openPossibility;
    private Boolean isBookmarked;
    private Long visitSuccessfulCount;
    private Long visitFailCount;
    private Long bookmarkCount;
    private List<MostVisitMemberResponse> mostVisitMembers;
}
