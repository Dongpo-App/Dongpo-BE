package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "점포 영업 시간")
public class OperatingTime {
    @Schema(description = "오픈 시간", example = "09:00")
    @NotNull
    private LocalTime openTime;

    @Schema(description = "마감 시간", example = "21:00")
    @NotNull
    private LocalTime closeTime;
}
