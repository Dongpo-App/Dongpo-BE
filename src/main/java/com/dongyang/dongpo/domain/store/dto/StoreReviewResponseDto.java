package com.dongyang.dongpo.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReviewResponseDto {
    private Long id;
    private Long memberId;
    private String memberNickname;
    private String memberMainTitle;
    private String memberProfilePic;
    private Double reviewStar;
    private String reviewText;
    private List<String> reviewPics;
    private LocalDateTime registerDate;
}
