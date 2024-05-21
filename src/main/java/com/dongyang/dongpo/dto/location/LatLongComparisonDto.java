package com.dongyang.dongpo.dto.location;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LatLongComparisonDto {
    private double latitude; // 위도
    private double longitude; // 경도
    private Long targetStoreId; // 위치를 비교하고자 하는 상점의 ID
}
