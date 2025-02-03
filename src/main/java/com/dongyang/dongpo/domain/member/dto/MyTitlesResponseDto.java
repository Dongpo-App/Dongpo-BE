package com.dongyang.dongpo.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "칭호 응답 DTO")
public class MyTitlesResponseDto {

    @Schema(description = "칭호 설명", example = "막 개장한 포장마차")
    @NotNull
    private String description;

    @Schema(description = "칭호 획득 조건", example = "첫 가입 시")
    @NotNull
    private String achieveCondition;

    @Schema(description = "칭호 획득 일자", example = "2021-08-01T00:00:00")
    @NotNull
    private LocalDateTime achieveDate;
}
