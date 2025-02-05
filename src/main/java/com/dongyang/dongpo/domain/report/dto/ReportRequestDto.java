package com.dongyang.dongpo.domain.report.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.report.entity.Report;
import com.dongyang.dongpo.domain.report.enums.ReportReason;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "신고 요청 DTO")
public class ReportRequestDto {

    @Schema(description = "신고 타입", example = "STORE")
    @NotNull
    private ReportType type;

    @Schema(description = "신고 대상 ID", example = "1")
    @NotNull
    private Long targetId;

    @Schema(description = "신고 사유", example = "SPAM")
    @NotNull
    private ReportReason reason;

    @Schema(description = "신고 내용", example = "도배글입니다.")
    private String text;

    public Report toEntity(final Member member) {
        return Report.builder()
                .member(member)
                .reason(this.reason)
                .type(this.type)
                .text(this.text)
                .targetId(this.targetId)
                .build();
    }
}
