package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.member.StoreVisitCert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreVisitCertRepository extends JpaRepository<StoreVisitCert, Long> {

    @Query("SELECT sr.memberId.nickname, COUNT(sr) as visitCount FROM StoreVisitCert sr WHERE sr.isVisitSuccessful = true GROUP BY sr.memberId.nickname ORDER BY visitCount DESC")
    List<Object[]> findTop10MembersBySuccessfulVisitCount();

}
