package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.StoreVisitCert;
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

}
