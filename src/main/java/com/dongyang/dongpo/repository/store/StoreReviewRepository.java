package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {

    List<StoreReview> findByStoreId(Long StoreId);
    List<StoreReview> findByMemberId(Long memberId);
}
