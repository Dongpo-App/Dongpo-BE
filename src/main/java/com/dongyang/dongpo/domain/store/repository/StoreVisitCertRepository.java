package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreVisitCert;
import com.dongyang.dongpo.domain.store.enums.TimeRange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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

    // 현재 시간대, 요일에 방문 인증 성공한 데이터만 필터링하여 갯수 반환
    @Query("SELECT COUNT(svc) FROM StoreVisitCert svc " +
            "WHERE svc.store = :store AND svc.isVisitSuccessful = true " +
            "AND svc.certTimeRange = :timeRange AND svc.certDay = :dayOfWeek")
    Long countVisitCertSuccessByStoreAndTimeRangeAndDayOfWeek(@Param("store") final Store store,
                                                              @Param("timeRange") final TimeRange timeRange,
                                                              @Param("dayOfWeek") final DayOfWeek dayOfWeek);

    // 가장 최근 방문인증 성공 내역
    Optional<StoreVisitCert> findTopByStoreAndMemberOrderByCertDateDesc(Store store, Member member);

    // 24시간 이내 방문인증 내역 존재 여부
    @Query("SELECT COUNT(svc) > 0 FROM StoreVisitCert svc " +
            "WHERE svc.store = :store AND svc.member = :member AND svc.certDate >= :timeRangeStart")
    Boolean existsByStoreAndMemberWithin24Hours(@Param("store") final Store store,
                                                @Param("member") final Member member,
                                                @Param("timeRangeStart") final LocalDateTime timeRangeStart);

    // 사용자의 방문 인증 성공 카운트
    Long countByMemberAndIsVisitSuccessfulIsTrue(Member member);

    // 사용자의 방문 인증 실패 카운트
    Long countByMemberAndIsVisitSuccessfulIsFalse(Member member);
}
