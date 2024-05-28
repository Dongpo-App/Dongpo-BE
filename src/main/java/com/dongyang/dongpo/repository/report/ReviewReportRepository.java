package com.dongyang.dongpo.repository.report;

import com.dongyang.dongpo.domain.report.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    List<ReviewReport> findByMemberId(Long memberId);
}
