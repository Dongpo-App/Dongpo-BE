package com.dongyang.dongpo.dto.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {
    private String nickname;
    private String profilePic;
    private TitleDto mainTitle; // 사용자 메인 칭호
    private List<TitleDto> titles; // 사용자 칭호 모음
    private Long registerCount;
    private Long titleCount;
    private Long presentCount;

    @Builder
    public static class TitleDto {
        private Title title;
        private String description;
    }

    public static MyPageDto toEntity(Member member, List<MemberTitle> memberTitles) {
        List<TitleDto> titles = memberTitles.stream()
                .map(title -> TitleDto.builder()
                        .title(title.getTitle())
                        .description(title.getTitle().getDescription())
                        .build())
                .toList();
        System.out.println("titles = " + titles);
        return MyPageDto.builder()
                .nickname(member.getNickname())
                .profilePic(member.getProfilePic())
                .mainTitle(TitleDto.builder()
                        .title(member.getMainTitle())
                        .description(member.getMainTitle().getDescription())
                        .build())
                .titles(titles)
                .registerCount(0L) // test
                .titleCount(0L) // test
                .presentCount(0L) // test
                .build();
    }
}
