package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.StoreReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {

    List<StoreReview> findByStoreId(Long storeId);

    @Query("SELECT sr FROM StoreReview sr " +
            "JOIN FETCH sr.member m " +
            "LEFT JOIN FETCH sr.reviewPics rp " +
            "WHERE sr.store.id = :storeId AND sr.status = 'VISIBLE'" +
            "ORDER BY sr.id DESC")
    List<StoreReview> findReviewWithDetailsByStoreDesc(@Param("storeId") Long storeId);

    List<StoreReview> findByMemberId(Long memberId);

    @Query("SELECT s.member, COUNT(s) as reviewCount " +
            "FROM StoreReview s " +
            "GROUP BY s.member " +
            "ORDER BY reviewCount DESC")
    List<Object[]> findTop10MembersByReviewCount(Pageable pageable);

    Long countByMember(Member member);

    List<StoreReview> findByMember(Member member);

    @Query("SELECT sr FROM StoreReview sr LEFT JOIN FETCH sr.reviewPics LEFT JOIN FETCH sr.store WHERE sr.member = :member")
    List<StoreReview> findByMemberWithReviewPicsAndStore(@Param("member") Member member);

    @Query("SELECT sr.id FROM StoreReview sr WHERE sr.store.id = :storeId ORDER BY sr.id DESC")
    List<Long> findTop3LatestReviewIdsByStoreId(@Param("storeId") Long storeId, Pageable pageable);

    @Query("SELECT sr FROM StoreReview sr JOIN FETCH sr.member m LEFT JOIN FETCH sr.reviewPics rp " +
            "WHERE sr.id IN :reviewIds ORDER BY sr.id DESC")
    List<StoreReview> findReviewsWithPicsByIds(@Param("reviewIds") List<Long> reviewIds);
}
