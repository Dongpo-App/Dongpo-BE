package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "점포 방문 인증 요청 DTO")
public class StoreVisitCertDto {

    @NotNull
    @Schema(description = "위도", example = "37.123456")
    private Double latitude; // 위도

    @NotNull
    @Schema(description = "경도", example = "127.123456")
    private Double longitude; // 경도

    @NotNull
    @Schema(description = "점포 ID", example = "1")
    private Long storeId;

    @NotNull
    @Schema(description = "방문 성공 여부", example = "true")
    private Boolean isVisitSuccessful;

}
