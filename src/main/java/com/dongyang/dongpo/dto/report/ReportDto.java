package com.dongyang.dongpo.dto.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.report.ReviewReport;
import com.dongyang.dongpo.domain.report.StoreReport;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDto {

    private Long id;
    private Long memberId;
    private Long reviewId;
    private Long storeId;
    private String text;


    public StoreReport toStoreEntity(Member member, Store store ) {
        return StoreReport.builder()
                .store(store)
                .member(member)
                .text(text)
                .build();
    }

    public ReviewReport toReviewEntity(Member member, StoreReview review){
        return ReviewReport.builder()
                .member(member)
                .review(review)
                .text(text)
                .build();
    }
}
