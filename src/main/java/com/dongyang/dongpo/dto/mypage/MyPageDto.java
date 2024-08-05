package com.dongyang.dongpo.dto.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {
    private String nickname;
    private String profilePic;
    @JsonProperty("mainTitle")
    private TitleDto mainTitle; // 사용자 메인 칭호
    private List<TitleDto> titles; // 사용자 칭호 모음
    private int registerCount;
    private int titleCount;
    private int presentCount;

    @Data
    @JsonSerialize
    @JsonDeserialize
    @Builder
    public static class TitleDto {
        private Title title;
        private String description;
    }

    public static MyPageDto toEntity(Member member, List<MemberTitle> memberTitles, List<Store> memberStores) {
        List<TitleDto> titles = memberTitles.stream()
                .map(title -> TitleDto.builder()
                        .title(title.getTitle())
                        .description(title.getTitle().getDescription())
                        .build())
                .toList();
        return MyPageDto.builder()
                .nickname(member.getNickname())
                .profilePic(member.getProfilePic())
                .mainTitle(TitleDto.builder()
                        .title(member.getMainTitle())
                        .description(member.getMainTitle().getDescription())
                        .build())
                .titles(titles)
                .registerCount(memberStores.size())
                .titleCount(memberTitles.size())
                .presentCount(0) // test
                .build();
    }
}
