package com.dongyang.dongpo.domain.member.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TitleDto {
        private Title title;
        private String description;
        private String achieveCondition;
        private LocalDateTime achieveDate;
    }

    public static TitleDto toTitleDto(MemberTitle memberTitle) {
        return TitleDto.builder()
                .title(memberTitle.getTitle())
                .description(memberTitle.getTitle().getDescription())
                .achieveCondition(memberTitle.getTitle().getAchieveCondition())
                .achieveDate(memberTitle.getAchieveDate())
                .build();
    }

    public static MyPageDto toEntity(Member member, List<MemberTitle> memberTitles, Long storeRegisterCount) {
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
                .registerCount(storeRegisterCount.intValue())
                .titleCount(memberTitles.size())
                .presentCount(0) // test
                .build();
    }
}
