package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreReviewPic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreReviewPicRepository extends JpaRepository<StoreReviewPic, Long> {
    @Query("SELECT rp FROM StoreReviewPic rp WHERE rp.store = :store ORDER BY rp.id DESC")
    List<StoreReviewPic> findTop10ByStoreOrderByIdDesc(@Param("store") Store store, Pageable pageable);
}
