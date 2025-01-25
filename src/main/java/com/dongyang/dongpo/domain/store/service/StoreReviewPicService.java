package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.domain.store.entity.StoreReviewPic;
import com.dongyang.dongpo.domain.store.repository.StoreReviewPicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreReviewPicService {

    private final StoreReviewPicRepository storeReviewPicRepository;

    List<StoreReviewPic> getTop5LatestReviewPics(Long storeId) {
        Pageable pageable = PageRequest.of(0, 5);
        return storeReviewPicRepository.findTop5ByStoreIdOrderByRegisterDateDesc(storeId, pageable);
    }

}
