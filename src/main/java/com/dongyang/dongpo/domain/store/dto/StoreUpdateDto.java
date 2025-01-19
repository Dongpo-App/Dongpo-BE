package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import com.dongyang.dongpo.domain.store.enums.StoreStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점포 정보 수정 요청 DTO")
public class StoreUpdateDto {

    @NotBlank
    @Schema(description = "점포명", example = "동포식당")
    private String name;

    @NotNull
    @Schema(description = "오픈 시간", example = "09:00")
    private LocalTime openTime;

    @NotNull
    @Schema(description = "마감 시간", example = "21:00")
    private LocalTime closeTime;

    @NotNull
    @Schema(description = "화장실 여부", example = "true")
    private Boolean isToiletValid;

    @NotNull
    @Schema(description = "점포 영업 상태", example = "OPEN")
    private StoreStatus status;

    @NotEmpty
    @Schema(description = "오픈 요일", example = "[\"MON\", \"TUE\"]")
    @Valid
    private List<OperatingDay> operatingDays;

    @NotEmpty
    @Schema(description = "결제 수단", example = "[\"CASH\", \"CARD\"]")
    @Valid
    private List<PayMethod> payMethods;

}
