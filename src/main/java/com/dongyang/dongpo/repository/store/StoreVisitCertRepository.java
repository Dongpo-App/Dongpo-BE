package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreVisitCertRepository extends JpaRepository<StoreVisitCert, Long> {

    @Query("SELECT s.member, COUNT(s) as visitCount " +
            "FROM StoreVisitCert s " +
            "WHERE s.isVisitSuccessful = true " +
            "GROUP BY s.member " +
            "ORDER BY visitCount DESC")
    List<Object[]> findTop10MembersBySuccessfulVisitCount(Pageable pageable);

    @Query("SELECT svc.member FROM StoreVisitCert svc " +
        "WHERE svc.store = :store AND svc.isVisitSuccessful = true " +
        "GROUP BY svc.member " +
        "ORDER BY COUNT(svc.member) DESC")
    List<Member> findTopVisitorsByStore(@Param("store") Store store);

    List<StoreVisitCert> findByStoreAndAndIsVisitSuccessfulTrue(Store store);

    // 가장 최근 방문인증 성공 내역
    Optional<StoreVisitCert> findTopByStoreAndMemberOrderByCertDateDesc(Store store, Member member);

    Long countByStoreAndIsVisitSuccessfulTrue(Store store); // 점포 방문인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsTrue(Member member); // 방문 인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsFalse(Member member); // 방문 인증 실패 카운트
}
