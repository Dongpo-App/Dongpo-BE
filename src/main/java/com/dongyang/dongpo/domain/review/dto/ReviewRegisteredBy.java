package com.dongyang.dongpo.domain.review.dto;

import com.dongyang.dongpo.domain.store.dto.RegisteredBy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Schema(description = "리뷰 등록자 정보")
public class ReviewRegisteredBy extends RegisteredBy {
    @Schema(description = "등록자 메인 칭호", example = "프로 리뷰어")
    @NotNull
    private String memberMainTitle;

    @Schema(description = "등록자 프로필 사진", example = "https://example.com/profile.jpg")
    @Nullable
    private String memberProfilePic;
}
