package com.dongyang.dongpo.domain.report.dto;

import com.dongyang.dongpo.domain.report.enums.ReportReason;
import com.dongyang.dongpo.domain.report.enums.ReportStatus;
import com.dongyang.dongpo.domain.report.enums.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Schema(description = "신고 정보 응답 DTO")
public class ReportResponseDto {

    @Schema(description = "신고 ID")
    @NotNull
    private Long id;

    @Schema(description = "신고자 ID")
    @NotNull
    private Long memberId;

    @Schema(description = "신고 사유")
    @NotNull
    private ReportReason reason;

    @Schema(description = "신고 내용")
    private String text;

    @Schema(description = "신고 타입")
    @NotNull
    private ReportType type;

    @Schema(description = "신고 대상 ID")
    @NotNull
    private Long targetId;

    @Schema(description = "신고 일자")
    @NotNull
    private LocalDateTime issueDate;

    @Schema(description = "신고 상태")
    @NotNull
    private ReportStatus status;

}
