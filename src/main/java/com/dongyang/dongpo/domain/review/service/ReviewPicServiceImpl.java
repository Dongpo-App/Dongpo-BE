package com.dongyang.dongpo.domain.review.service;

import com.dongyang.dongpo.domain.review.entity.ReviewPic;
import com.dongyang.dongpo.domain.review.repository.ReviewPicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewPicServiceImpl implements ReviewPicService {

    private final ReviewPicRepository reviewPicRepository;

    public List<ReviewPic> getTop5LatestReviewPics(final Long storeId) {
        Pageable pageable = PageRequest.of(0, 5);
        return reviewPicRepository.findTop5ByStoreIdOrderByRegisterDateDesc(storeId, pageable);
    }

}
