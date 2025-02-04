package com.dongyang.dongpo.domain.report.entity;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.dto.ReportResponseDto;
import com.dongyang.dongpo.domain.report.enums.ReportReason;
import com.dongyang.dongpo.domain.report.enums.ReportStatus;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "reports")
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_member_id")
    @NotNull
    private Member member;

    @Column(name = "report_reason", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "report_type", columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ReportType type;

    @Column(name = "target_id")
    @NotNull
    private Long targetId;

    @CreatedDate
    @Column(name = "issue_date")
    @NotNull
    private LocalDateTime issueDate;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.OPEN;

    public ReportResponseDto toResponseDto() {
        return ReportResponseDto.builder()
                .id(this.id)
                .memberId(member.getId())
                .reason(this.reason)
                .text(this.text)
                .type(this.type)
                .targetId(this.targetId)
                .issueDate(this.issueDate)
                .status(this.status)
                .build();
    }
}
