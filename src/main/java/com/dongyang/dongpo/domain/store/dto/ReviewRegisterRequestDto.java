package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreReview;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리뷰 등록 요청 DTO")
public class ReviewRegisterRequestDto {

    @NotNull
    @Schema(description = "리뷰 별점", example = "4.5")
    private Double reviewStar;

    @NotNull
    @Schema(description = "리뷰 내용", example = "맛있어요")
    private String reviewText;

    @Nullable
    @Schema(description = "리뷰 사진 URL 리스트", example = "[\"http://localhost:8080/api/files/1\", \"http://localhost:8080/api/files/2\"]")
    private List<String> reviewPics;

    public StoreReview toEntity(final Store store, final Member member) {
        return StoreReview.builder()
                .member(member)
                .store(store)
                .text(this.reviewText)
                .reviewStar(this.reviewStar)
                .build();
    }
}
