package com.dongyang.dongpo.common.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LatLong {
    @NotNull
    @Schema(description = "위도", example = "37.123456")
    private Double latitude; // 위도

    @NotNull
    @Schema(description = "경도", example = "127.123456")
    private Double longitude; // 경도
}
