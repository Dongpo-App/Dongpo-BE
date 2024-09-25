package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.OpenTime;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreVisitCert;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface StoreVisitCertRepository extends JpaRepository<StoreVisitCert, Long> {

    @Query("SELECT s.member, COUNT(s) as visitCount " +
            "FROM StoreVisitCert s " +
            "WHERE s.isVisitSuccessful = true " +
            "GROUP BY s.member " +
            "ORDER BY visitCount DESC")
    List<Object[]> findTop10MembersBySuccessfulVisitCount(Pageable pageable);

    Optional<StoreVisitCert> findByStore(Store store);

    // 해당 요일, 시간에 방문 인증 성공한 데이터가 존재하는지 확인
    boolean existsByOpenDayAndOpenTimeAndIsVisitSuccessfulTrue(DayOfWeek dayOfWeek, OpenTime openTime);

    Long countByStoreAndIsVisitSuccessfulTrue(Store store); // 점포 방문인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsTrue(Member member); // 방문 인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsFalse(Member member); // 방문 인증 실패 카운트
}
