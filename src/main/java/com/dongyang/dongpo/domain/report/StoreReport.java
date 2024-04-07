package com.dongyang.dongpo.domain.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_report")
public class StoreReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_writer")
    private Member reportWriter;

    @Column(columnDefinition = "TEXT")
    private String reportText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_store_id")
    private Store reportingStoreId;

    private LocalDateTime issueDate;

    @Column(length = 24)
    private String issueIp;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.OPEN;

    public enum ReportStatus {
        OPEN, PROCESSING, CLOSED
    }
}
