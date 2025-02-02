package com.dongyang.dongpo.domain.bookmark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "북마크 추가/삭제 응답 DTO")
public class BookmarkResponseDto {

    @Schema(description = "점포 북마크 수", example = "1")
    @NotNull
    private Long bookmarkCount;

    @Schema(description = "사용자의 북마크 여부", example = "true")
    @NotNull
    private Boolean isMemberBookmarked;

}
