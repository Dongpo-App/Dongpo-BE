package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점포 등록 요청 DTO")
public class StoreRegisterDto {

    @NotBlank
    @Schema(description = "점포명", example = "동포식당")
    private String name;

    @NotBlank
    @Schema(description = "주소", example = "서울특별시 강남구 역삼동")
    private String address;

    @NotNull
    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90.0")
    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180.0")
    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @NotNull
    @Schema(description = "오픈 시간", example = "09:00")
    private LocalTime openTime;

    @NotNull
    @Schema(description = "마감 시간", example = "21:00")
    private LocalTime closeTime;

    @NotNull
    @Schema(description = "화장실 여부", example = "true")
    private Boolean isToiletValid;

    @NotEmpty
    @Valid
    @Schema(description = "오픈 요일", example = "[\"MON\", \"TUE\"]")
    private List<OperatingDay> operatingDays;

    @NotEmpty
    @Valid
    @Schema(description = "결제 수단", example = "[\"CASH\", \"CARD\"]")
    private List<PayMethod> payMethods;

    @NotNull
    @DecimalMin(value = "-90.0", message = "Current latitude must be greater than or equal to -90.0")
    @DecimalMax(value = "90.0", message = "Current latitude must be less than or equal to 90.0")
    @Schema(description = "사용자의 현재 위도", example = "37.123456")
    private Double currentLatitude;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Current longitude must be greater than or equal to -180.0")
    @DecimalMax(value = "180.0", message = "Current longitude must be less than or equal to 180.0")
    @Schema(description = "사용자의 현재 경도", example = "127.123456")
    private Double currentLongitude;


    public Store toEntity(Member member) {
        return Store.builder()
                .name(this.name)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .openTime(this.openTime)
                .closeTime(this.closeTime)
                .member(member)
                .isToiletValid(this.isToiletValid)
                .build();
    }
}
