package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점포 상세 정보 조회 응답 DTO")
public class StoreDetailInfoResponseDto extends StoreBasicInfoResponseDto {

    @Schema(description = "영업 시간", example = "09:00 ~ 21:00")
    @NotEmpty
    @Valid
    private OperatingTime operatingTime;

    @Schema(description = "점포 등록자 정보")
    @NotEmpty
    @Valid
    private RegisteredBy registeredBy;

    @Schema(description = "화장실 여부", example = "true")
    @NotNull
    private Boolean isToiletValid;

    @Schema(description = "결제 수단", example = "[\"CASH\", \"CARD\"]")
    @NotEmpty
    @Valid
    private List<Store.PayMethod> payMethods;

    @Schema(description = "오픈 가능성", example = "OPEN")
    @NotNull
    @Valid
    private OpenPossibility openPossibility;

    @Schema(description = "방문 성공 횟수", example = "100")
    @NotNull
    private Long visitSuccessCount;

    @Schema(description = "방문 실패 횟수", example = "10")
    @NotNull
    private Long visitFailCount;

    @Schema(description = "북마크 수", example = "100")
    @NotNull
    private Long bookmarkCount;

    @Schema(description = "영업 요일", example = "[\"MON\", \"TUE\"]")
    @NotEmpty
    @Valid
    private List<Store.OperatingDay> operatingDays;
}
