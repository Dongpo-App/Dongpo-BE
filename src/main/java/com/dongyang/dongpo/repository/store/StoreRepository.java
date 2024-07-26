package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByMemberId(Long memberId);

    @Query("SELECT s.member.nickname, COUNT(s) as storeCount FROM Store s GROUP BY s.member.nickname ORDER BY storeCount DESC")
    List<Object[]> findTop10MembersByStoreCount();
}
