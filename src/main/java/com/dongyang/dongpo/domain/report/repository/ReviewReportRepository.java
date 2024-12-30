package com.dongyang.dongpo.domain.report.repository;

import com.dongyang.dongpo.domain.report.entity.ReviewReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    List<ReviewReport> findByMemberId(Long memberId);
}
