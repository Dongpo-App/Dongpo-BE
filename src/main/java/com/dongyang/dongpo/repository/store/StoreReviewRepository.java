package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.StoreReview;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {

    List<StoreReview> findByStoreId(Long StoreId);
    List<StoreReview> findByMemberId(Long memberId);

    @Query("SELECT sr.member.nickname, COUNT(sr) as reviewCount FROM StoreReview sr GROUP BY sr.member.nickname ORDER BY reviewCount DESC")
    List<Object[]> findTop10MembersByReviewCount();
}
