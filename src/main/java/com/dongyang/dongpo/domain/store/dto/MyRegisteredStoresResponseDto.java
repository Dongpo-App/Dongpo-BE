package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "내가 등록한 점포 조회 DTO")
public class MyRegisteredStoresResponseDto {

    @Schema(description = "점포 ID", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "점포 이름", example = "동포")
    @NotNull
    private String name;

    @Schema(description = "주소", example = "서울시 구로구 고척동")
    @NotNull
    private String address;

    @Schema(description = "등록일", example = "2021-07-01T00:00:00")
    @NotNull
    private LocalDateTime registerDate;
}
