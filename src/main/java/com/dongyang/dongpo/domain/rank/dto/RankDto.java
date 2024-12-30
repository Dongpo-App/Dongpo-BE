package com.dongyang.dongpo.domain.rank.dto;

import com.dongyang.dongpo.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankDto {

    private String nickname;
    private String title;
    private String pic;
    private Long count;

    public static RankDto toDto(Object[] obj) {
        Member member = (Member) obj[0];
        Long count = (Long) obj[1];

        return RankDto.builder()
                .nickname(member.getNickname())
                .title(member.getMainTitle().getDescription())
                .pic(member.getProfilePic())
                .count(count)
                .build();
    }
}
