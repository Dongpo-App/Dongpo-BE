package com.dongyang.dongpo.dto.mypage;

import com.dongyang.dongpo.domain.member.Member;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {
    private String nickname;
    private String profilePic;
//    private String title; // 칭호
    private Long registerCount;
    private Long titleCount;
    private Long presentCount;

    public static MyPageDto toEntity(Member member) {
        return MyPageDto.builder()
                .nickname(member.getNickname())
                .profilePic(member.getProfilePic())
                .registerCount(0L) // test
                .titleCount(0L) // test
                .presentCount(0L) // test
                .build();
    }
}
