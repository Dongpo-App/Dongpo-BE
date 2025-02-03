package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.enums.Title;
import com.dongyang.dongpo.domain.member.repository.MemberTitleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TitleServiceImpl implements TitleService {

    private final MemberTitleRepository memberTitleRepository;

    // 사용자 칭호 추가
    @Override
    @Transactional
    public void addTitle(final Member member, final Title title) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMemberOrderByIdDesc(member);
        if (memberTitles.stream().noneMatch(t -> t.getTitle().equals(title))) {
            memberTitleRepository.save(MemberTitle.builder()
                    .title(title)
                    .achieveDate(LocalDateTime.now())
                    .member(member)
                    .build());

            log.info("member {} add title : {}", member.getId(), title.getDescription());
        }
    }

    // 사용자 칭호 조회
    @Override
    public List<MyTitlesResponseDto> getMemberTitles(final Member member) {
        return memberTitleRepository.findByMemberOrderByIdDesc(member)
                .stream()
                .map(MemberTitle::toMyTitlesResponse)
                .toList();
    }

    // 사용자 칭호 개수 조회
    @Override
    public Long getMemberTitlesCount(final Member member) {
        return memberTitleRepository.countByMember(member);
    }

    // 사용자와 칭호로 존재 여부 확인
    @Override
    public MemberTitle findByMemberAndTitle(final Member member, final Title title) {
        return memberTitleRepository.findByMemberAndTitle(member, title)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_TITLE_NOT_FOUND));
    }
}
