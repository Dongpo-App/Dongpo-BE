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
    private Integer reviewStar;
    private String text;
    private List<String> reviewPics;
    private LocalDateTime registerDate;
    private StoreReview.ReviewStatus status;
    private Integer reportCount;

    public StoreReview toEntity(Store store, Member member) {
        StoreReview storeReview = StoreReview.builder()
            .member(member)
            .store(store)
            .text(text)
            .reviewStar(reviewStar)
            .build();

        reviewPics.forEach(picUrl -> {
            StoreReviewPic pic = StoreReviewPic.builder()
                .picUrl(picUrl)
                .build();
            storeReview.addReviewPic(pic);
        });

        return storeReview;
    }

    public static ReviewDto toDto(StoreReview storeReview){
        List<String> picUrlList = storeReview.getReviewPics().stream().map(StoreReviewPic::getPicUrl).toList();

        return ReviewDto.builder()
                .id(storeReview.getId())
                .storeId(storeReview.getStore().getId())
                .memberId(storeReview.getMember().getId())
                .reviewStar(storeReview.getReviewStar())
                .text(storeReview.getText())
                .reviewPics(picUrlList)
                .registerDate(storeReview.getRegisterDate())
                .status(storeReview.getStatus())
                .reportCount(storeReview.getReportCount())
                .build();
    }
}
