package com.dongyang.dongpo.domain.review.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStoreId(final Long storeId);

    @Query("SELECT rv FROM Review rv " +
            "JOIN FETCH rv.member m " +
            "LEFT JOIN FETCH rv.reviewPics rp " +
            "WHERE rv.id IN :reviewIds " +
            "ORDER BY rv.id DESC")
    List<Review> findReviewsWithDetailsByIdsDesc(@Param("reviewIds") final List<Long> reviewIds);

    @Query("SELECT rv.id FROM Review rv " +
            "WHERE rv.store = :store AND rv.status = 'VISIBLE' ORDER BY rv.id DESC")
    Page<Long> findReviewIdsByStoreAndPageRequest(@Param("store") final Store Store, final Pageable pageable);

    List<Review> findByMemberId(final Long memberId);

    @Query("SELECT rv.member, COUNT(rv) as reviewCount " +
            "FROM Review rv " +
            "GROUP BY rv.member " +
            "ORDER BY reviewCount DESC")
    List<Object[]> findTop10MembersByReviewCount(final Pageable pageable);

    Long countByMember(final Member member);

    List<Review> findByMember(final Member member);

    @Query("SELECT rv FROM Review rv LEFT JOIN FETCH rv.reviewPics LEFT JOIN FETCH rv.store WHERE rv.member = :member")
    List<Review> findByMemberWithReviewPicsAndStore(@Param("member") final Member member);

}
