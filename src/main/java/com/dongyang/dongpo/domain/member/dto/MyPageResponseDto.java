package com.dongyang.dongpo.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "마이페이지 응답 DTO")
public class MyPageResponseDto {

    @Schema(description = "닉네임", example = "동양")
    @NotNull
    private String nickname;

    @Schema(description = "프로필 사진 URL", example = "https://example.com/profile.jpg")
    @NotNull
    private String profilePic;

    @Schema(description = "메인 칭호", example = "막 개장한 포장마차")
    @NotNull
    private String mainTitle; // 사용자 메인 칭호

    @Schema(description = "점포 등록 횟수", example = "3")
    @NotNull
    private Long registerCount;

    @Schema(description = "보유 칭호 수", example = "2")
    @NotNull
    private Long titleCount;

}
