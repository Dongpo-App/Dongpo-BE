package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import com.dongyang.dongpo.service.store.StoreReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final StoreReviewRepository reviewRepository;

    public List<StoreReview> findAll(){
        return reviewRepository.findAll();
    }
}
