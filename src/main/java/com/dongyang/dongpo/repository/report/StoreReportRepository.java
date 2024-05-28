package com.dongyang.dongpo.repository.report;

import com.dongyang.dongpo.domain.report.StoreReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreReportRepository extends JpaRepository<StoreReport, Long> {
    List<StoreReport> findByStoreId(Long storeId);
    List<StoreReport> findByMemberId(Long memberId);
}
