package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.store.enums.ReviewStatus;
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
    private String reviewText;
    private List<String> reviewPics;
    private LocalDateTime registerDate;
    private ReviewStatus status;
    private Integer reportCount;
}
