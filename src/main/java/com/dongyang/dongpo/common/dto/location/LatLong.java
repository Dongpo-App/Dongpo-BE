package com.dongyang.dongpo.common.dto.location;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LatLong {
    private double latitude; // 위도
    private double longitude; // 경도
}
