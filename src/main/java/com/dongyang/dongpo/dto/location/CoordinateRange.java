package com.dongyang.dongpo.dto.location;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoordinateRange {
    private double minLat;
    private double maxLat;
    private double minLong;
    private double maxLong;
}
