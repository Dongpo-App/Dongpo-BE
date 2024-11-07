package com.dongyang.dongpo.service.store;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import com.dongyang.dongpo.repository.store.StoreReviewPicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreReviewPicService {

    private final StoreReviewPicRepository storeReviewPicRepository;

    public List<String> getReviewPicsByStore(Store store) {
        return storeReviewPicRepository.findTop10ByStoreOrderByIdDesc(store, PageRequest.of(0, 10))
                .stream()
                .map(StoreReviewPic::getPicUrl)
                .toList();
    }

}
