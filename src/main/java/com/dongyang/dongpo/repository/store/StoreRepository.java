package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByMember(Member member);

    @Query(value = "SELECT * FROM store_table WHERE (latitude BETWEEN :minLat AND :maxLat) " +
                    "AND (longitude BETWEEN :minLong AND :maxLong)", nativeQuery = true)
    List<Store> findStoresWithinRange(@Param("minLat") double minLat,
                                      @Param("maxLat") double maxLat,
                                      @Param("minLong") double minLong,
                                      @Param("maxLong") double maxLong);

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
