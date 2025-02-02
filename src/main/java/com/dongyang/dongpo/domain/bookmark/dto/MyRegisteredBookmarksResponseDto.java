package com.dongyang.dongpo.domain.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "내가 등록한 북마크 조회 DTO")
public class MyRegisteredBookmarksResponseDto {

    @Schema(description = "북마크 ID", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "점포 ID", example = "1")
    @NotNull
    private Long storeId;

    @Schema(description = "점포 이름", example = "동포")
    @NotNull
    private String storeName;

    @Schema(description = "북마크 등록일", example = "2021-07-01T00:00:00")
    @NotNull
    private LocalDateTime bookmarkDate;

}
