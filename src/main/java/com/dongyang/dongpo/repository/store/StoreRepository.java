package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByMemberId(Long memberId);

    @Query(value = "SELECT * FROM store_table WHERE (latitude BETWEEN :minLat AND :maxLat) " +
                    "AND (longitude BETWEEN :minLong AND :maxLong)", nativeQuery = true)
    List<Store> findStoresWithinRange(@Param("minLat") double minLat,
                                      @Param("maxLat") double maxLat,
                                      @Param("minLong") double minLong,
                                      @Param("maxLong") double maxLong);

    @Query("SELECT s.member.nickname, COUNT(s) as storeCount FROM Store s GROUP BY s.member.nickname ORDER BY storeCount DESC")
    List<Object[]> findTop10MembersByStoreCount();
}
