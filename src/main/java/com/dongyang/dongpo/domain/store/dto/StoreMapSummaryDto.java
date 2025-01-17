package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점포 간략 정보 조회 DTO")
public class StoreMapSummaryDto {

    @Schema(description = "점포 ID", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "점포 이름", example = "동포")
    @NotBlank
    private String name;

    @Schema(description = "위도", example = "37.123456")
    @NotNull
    private Double latitude;

    @Schema(description = "경도", example = "127.123456")
    @NotNull
    private Double longitude;

    @Schema(description = "주소", example = "서울시 강남구")
    @NotBlank
    private String address;

    @Schema(description = "오픈 가능성", example = "OPEN")
    @NotNull
    private OpenPossibility openPossibility;

    @Schema(description = "북마크 여부", example = "true")
    @NotNull
    private Boolean isBookmarked;

    @Schema(description = "리뷰 사진 목록")
    @Nullable
    private List<String> reviewPics;
}
