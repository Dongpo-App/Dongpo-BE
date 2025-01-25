package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "점포 리뷰 조회 DTO")
public class StoreReviewResponseDto {

    @Schema(description = "리뷰 ID", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "리뷰 등록자 정보")
    @NotEmpty
    @Valid
    private ReviewRegisteredBy registeredBy;

    @Schema(description = "리뷰 별점", example = "4.5")
    @NotNull
    private Double reviewStar;

    @Schema(description = "리뷰 내용", example = "맛있어요")
    @NotBlank
    private String reviewText;

    @Schema(description = "리뷰 사진")
    @NotEmpty
    private List<String> reviewPics;

    @Schema(description = "리뷰 등록일", example = "2021-07-01T00:00:00")
    @NotNull
    private LocalDateTime registerDate;
}
