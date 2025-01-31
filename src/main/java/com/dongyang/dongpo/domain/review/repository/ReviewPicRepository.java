package com.dongyang.dongpo.domain.review.repository;

import com.dongyang.dongpo.domain.review.entity.ReviewPic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewPicRepository extends JpaRepository<ReviewPic, Long> {

    @Query("SELECT rp FROM ReviewPic rp JOIN rp.review r JOIN r.store s WHERE s.id = :storeId ORDER BY rp.registerDate DESC")
    List<ReviewPic> findTop5ByStoreIdOrderByRegisterDateDesc(@Param("storeId") Long storeId, Pageable pageable);
}
