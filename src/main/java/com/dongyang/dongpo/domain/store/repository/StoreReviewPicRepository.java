package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.store.entity.StoreReviewPic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreReviewPicRepository extends JpaRepository<StoreReviewPic, Long> {

    @Query("SELECT rp FROM StoreReviewPic rp JOIN rp.reviewId r JOIN r.store s WHERE s.id = :storeId ORDER BY rp.registerDate DESC")
    List<StoreReviewPic> findTop5ByStoreIdOrderByRegisterDateDesc(@Param("storeId") Long storeId, Pageable pageable);
}
