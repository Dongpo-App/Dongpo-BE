package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByMember(Member member);

    @Query("SELECT s, sb FROM Store s LEFT JOIN StoreBookmark sb " +
            "ON s.id = sb.store.id AND sb.member.id = :memberId " +
            "WHERE s.latitude BETWEEN :minLat AND :maxLat " +
            "AND s.longitude BETWEEN :minLong AND :maxLong " +
            "AND s.status = 'ACTIVE'")
    List<Object[]> findStoresWithBookmarksWithinRange(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
                                                      @Param("minLong") double minLong, @Param("maxLong") double maxLong,
                                                      @Param("memberId") Long memberId);

    @Query("SELECT s.member, COUNT(s) as storeCount " +
            "FROM Store s " +
            "GROUP BY s.member " +
            "ORDER BY storeCount DESC")
    List<Object[]> findTop10MembersByStoreCount(Pageable pageable);

    Long countByMember(Member member);

    @Query(value = "SELECT s.* " +
        "FROM store_visit_cert svc " +
        "JOIN member_table m ON svc.member_id = m.id " +
        "JOIN store_table s ON svc.cert_store = s.id " +
        "WHERE s.status = 'ACTIVE' " +
        "AND (YEAR(CURRENT_DATE) - CAST(m.birthyear AS UNSIGNED)) BETWEEN :ageGroup AND (:ageGroup + 9) " +
        "GROUP BY s.id " +
        "ORDER BY COUNT(svc.id) DESC",
    nativeQuery = true)
    List<Store> findStoresByMemberAgeWithMostVisits(@Param("ageGroup") int ageGroup, Pageable pageable);


    @Query("SELECT s " +
        "FROM StoreVisitCert svc " +
        "JOIN svc.member m " +
        "JOIN svc.store s " +
        "WHERE s.status = 'ACTIVE' " +
        "AND m.gender = :gender " +
        "GROUP BY s.id " +
        "ORDER BY COUNT(svc) DESC")
    List<Store> findStoresByMemberGenderWithMostVisits(@Param("gender") Member.Gender gender, Pageable pageable);
}
