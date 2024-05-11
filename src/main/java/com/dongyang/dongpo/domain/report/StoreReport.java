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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writerId;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store storeId;

    private LocalDateTime issueDate;

    @Column(length = 24)
    private String issueIp;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.OPEN;

    public enum ReportStatus {
        OPEN, PROCESSING, CLOSED
    }
}
