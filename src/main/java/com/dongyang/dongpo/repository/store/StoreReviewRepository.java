package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.StoreReview;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {

    List<StoreReview> findByStoreId(Long StoreId);
    List<StoreReview> findByMemberId(Long memberId);

    @Query("SELECT s.member, COUNT(s) as reviewCount " +
            "FROM StoreReview s " +
            "GROUP BY s.member " +
            "ORDER BY reviewCount DESC")
    List<Object[]> findTop10MembersByReviewCount(Pageable pageable);
}
