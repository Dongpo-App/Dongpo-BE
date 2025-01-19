package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NearbyStoresResponseDto", description = "사용자 주변 점포 응답 DTO")
public class NearbyStoresResponseDto {

    @NotNull
    @Schema(description = "점포 ID", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "점포명", example = "동포식당")
    private String name;

    @NotNull
    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @NotNull
    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @NotNull
    @Schema(description = "주소", example = "서울특별시 강남구 역삼동")
    private Boolean isBookmarked;
}
