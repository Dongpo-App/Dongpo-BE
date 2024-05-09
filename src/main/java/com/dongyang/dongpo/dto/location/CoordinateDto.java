package com.dongyang.dongpo.dto.location;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinateDto {
    private double lat; // 위도
    private double lng; // 경도
}
