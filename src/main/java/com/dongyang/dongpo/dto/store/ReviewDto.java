package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {
    private Long id;
    private Long storeId;
    private Long memberId;
    private Integer reviewStar;
    private String text;
    private String reviewPic;
    private LocalDateTime registerDate;
    private StoreReview.ReviewStatus status;
    private Integer reportCount;


    public StoreReview toEntity(Store store, Member member){
        return StoreReview.builder()
                .member(member)
                .store(store)
                .text(text)
                .reviewPic(reviewPic)
                .reviewStar(reviewStar)
                .build();
    }

    public static ReviewDto toDto(StoreReview storeReview){
        return ReviewDto.builder()
                .id(storeReview.getId())
                .storeId(storeReview.getStore().getId())
                .memberId(storeReview.getMember().getId())
                .reviewStar(storeReview.getReviewStar())
                .text(storeReview.getText())
                .reviewPic(storeReview.getReviewPic())
                .registerDate(storeReview.getRegisterDate())
                .status(storeReview.getStatus())
                .reportCount(storeReview.getReportCount())
                .build();
    }
}
