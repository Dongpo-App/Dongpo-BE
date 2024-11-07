package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {
    private Long id;
    private Long storeId;
    private String storeName;
    private Long memberId;
    private String memberNickname;
    private String memberMainTitle;
    private String memberProfilePic;
    private Double reviewStar;
    private String text;
    private List<String> reviewPics;
    private LocalDateTime registerDate;
    private StoreReview.ReviewStatus status;
    private Integer reportCount;

    public StoreReview toEntity(Store store, Member member) {
        return StoreReview.builder()
            .member(member)
            .store(store)
            .text(text)
            .reviewStar(reviewStar)
            .build();
    }
}
