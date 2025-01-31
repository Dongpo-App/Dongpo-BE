package com.dongyang.dongpo.domain.review.service;

import com.dongyang.dongpo.domain.review.entity.ReviewPic;

import java.util.List;

public interface ReviewPicService {
    List<ReviewPic> getTop5LatestReviewPics(Long storeId);
}
