package com.dongyang.dongpo.domain.member.dto;

import com.dongyang.dongpo.domain.member.enums.Title;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageUpdateRequestDto {

    @Schema(description = "변경할 닉네임", example = "동양")
    @NotNull
    private String nickname;

    @Schema(description = "변경할 프로필 사진 URL", example = "https://example.com/profile.jpg")
    @NotNull
    private String profilePic;

    @Schema(description = "변경할 메인 칭호", example = "막 개장한 포장마차")
    @NotNull
    private Title newMainTitle;
}
