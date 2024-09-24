package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreVisitCertRepository extends JpaRepository<StoreVisitCert, Long> {

    @Query("SELECT s.member, COUNT(s) as visitCount " +
            "FROM StoreVisitCert s " +
            "WHERE s.isVisitSuccessful = true " +
            "GROUP BY s.member " +
            "ORDER BY visitCount DESC")
    List<Object[]> findTop10MembersBySuccessfulVisitCount(Pageable pageable);

    Long countByStoreAndIsVisitSuccessfulTrue(Store store); // 점포 방문인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsTrue(Member member); // 방문 인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsFalse(Member member); // 방문 인증 실패 카운트
}
