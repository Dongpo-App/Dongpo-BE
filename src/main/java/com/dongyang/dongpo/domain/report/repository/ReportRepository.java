package com.dongyang.dongpo.domain.report.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.entity.Report;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("SELECT r FROM Report r WHERE r.member = :member AND r.type = :reportType ORDER BY r.id DESC")
    Page<Report> findByMemberAndType(@Param("member") Member member, @Param("reportType") ReportType reportType, Pageable pageable);

    List<Report> findByType(ReportType type);
}
