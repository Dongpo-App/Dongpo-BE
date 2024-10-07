package com.dongyang.dongpo.domain.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.report.ReportDto;
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
    private Member member;

    @Column(name = "report_reason")
    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder.Default
    private LocalDateTime issueDate = LocalDateTime.now();

    @Column(length = 24)
    private String issueIp;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.OPEN;

    public enum ReportStatus {
        OPEN, PROCESSING, CLOSED
    }

    public ReportDto toDto(){
        return ReportDto.builder()
                .id(id)
                .storeId(store.getId())
                .text(text)
                .memberId(member.getId())
                .issueDate(issueDate)
                .build();
    }
}
