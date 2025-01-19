package com.dongyang.dongpo.domain.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "점포 등록자 정보")
public class RegisteredBy {
    @Schema(description = "등록자 ID", example = "1")
    @NotNull
    private Long memberId;

    @Schema(description = "등록자 닉네임", example = "dongpo")
    @NotNull
    private String memberNickname;
}
