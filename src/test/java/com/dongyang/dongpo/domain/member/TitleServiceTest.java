package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.enums.Title;
import com.dongyang.dongpo.domain.member.repository.MemberTitleRepository;
import com.dongyang.dongpo.domain.member.service.TitleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceTest {

    @Mock
    private MemberTitleRepository memberTitleRepository;

    @InjectMocks
    private TitleService titleService;

    @Test
    @DisplayName("칭호_추가")
    void addTitle() {
        Member member = mock(Member.class);
        Title title = Title.FAILED_TO_VISIT;

        when(memberTitleRepository.findByMemberOrderByIdDesc(member)).thenReturn(List.of());

        titleService.addTitle(member, title);

        verify(memberTitleRepository).save(any(MemberTitle.class));
    }

    @Test
    @DisplayName("칭호_추가_중복")
    void addTitleDuplicate() {
        Member member = mock(Member.class);
        Title title = Title.FAILED_TO_VISIT;
        MemberTitle memberTitle = MemberTitle.builder()
                .title(title)
                .build();

        when(memberTitleRepository.findByMemberOrderByIdDesc(member)).thenReturn(List.of(memberTitle));

        titleService.addTitle(member, title);

        verify(memberTitleRepository, never()).save(any(MemberTitle.class));
    }

    @Test
    void getMemberTitles() {
        Member member = mock(Member.class);
        MemberTitle memberTitle1 = mock(MemberTitle.class);
        MemberTitle memberTitle2 = mock(MemberTitle.class);
        MyTitlesResponseDto titleDto1 = mock(MyTitlesResponseDto.class);
        MyTitlesResponseDto titleDto2 = mock(MyTitlesResponseDto.class);

        when(memberTitleRepository.findByMemberOrderByIdDesc(member)).thenReturn(List.of(memberTitle1, memberTitle2));
        when(memberTitle1.toMyTitlesResponse()).thenReturn(titleDto1);
        when(memberTitle2.toMyTitlesResponse()).thenReturn(titleDto2);

        List<MyTitlesResponseDto> memberTitles = titleService.getMemberTitles(member);

        assertThat(memberTitles).containsExactly(titleDto1, titleDto2);
        verify(memberTitleRepository).findByMemberOrderByIdDesc(member);
    }

    @Test
    void getMemberTitlesCount() {
        Member member = mock(Member.class);
        long expectedCount = 5L;

        when(memberTitleRepository.countByMember(member)).thenReturn(expectedCount);

        long count = titleService.getMemberTitlesCount(member);

        assertThat(count).isEqualTo(expectedCount);
        verify(memberTitleRepository).countByMember(member);
    }

    @Test
    void findByMemberAndTitle() {
        Member member = mock(Member.class);
        Title title = Title.FAILED_TO_VISIT;
        MemberTitle expectedMemberTitle = mock(MemberTitle.class);

        when(memberTitleRepository.findByMemberAndTitle(member, title)).thenReturn(Optional.ofNullable(expectedMemberTitle));

        MemberTitle result = titleService.findByMemberAndTitle(member, title);

        assertThat(result).isEqualTo(expectedMemberTitle);
        verify(memberTitleRepository).findByMemberAndTitle(member, title);
    }
}