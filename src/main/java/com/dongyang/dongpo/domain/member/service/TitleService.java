package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.enums.Title;

import java.util.List;

public interface TitleService {
    void addTitle(Member member, Title title);

    List<MyTitlesResponseDto> getMemberTitles(Member member);

    Long getMemberTitlesCount(Member member);

    MemberTitle findByMemberAndTitle(Member member, Title title);
}
