package com.dongyang.dongpo.domain.member.dto;

import com.dongyang.dongpo.domain.member.entity.Title;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageUpdateDto {
    private String nickname;
    private String profilePic;
    private Title newMainTitle;
}
