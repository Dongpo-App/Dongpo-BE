package com.dongyang.dongpo.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyRegisteredReviewsResponseDto {

    @NotNull
    @Schema(description = "리뷰 ID", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "점포 ID", example = "1")
    private Long storeId;

    @NotNull
    @Schema(description = "점포 이름", example = "맛집")
    private String storeName;

    @NotNull
    @Schema(description = "별점", example = "4.5")
    private Double reviewStar;

    @NotNull
    @Schema(description = "리뷰 내용", example = "맛있어요")
    private String reviewText;

    @Null
    @Schema(description = "리뷰 사진 URL 리스트", example = "[\"http://localhost:8080/api/files/1\", \"http://localhost:8080/api/files/2\"]")
    private List<String> reviewPics;

    @NotNull
    @Schema(description = "리뷰 등록일", example = "2021-08-01T00:00:00")
    private LocalDateTime registerDate;

}
