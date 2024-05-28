package com.dongyang.dongpo.domain.report;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.StoreReview;
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
@Table(name = "review_report")
public class ReviewReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_writer")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporting_review_id")
    private StoreReview review;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime issueDate = LocalDateTime.now();

    @Column(length = 24)
    private String issueIp;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.OPEN;



    public enum ReportStatus {
        OPEN, PROCEEDING, CLOSED
    }

    public ReportDto toDto() {
        return ReportDto.builder()
                .reviewId(review.getId())
                .memberId(member.getId())
                .text(text)
                .issueDate(issueDate)
                .build();
    }
}
