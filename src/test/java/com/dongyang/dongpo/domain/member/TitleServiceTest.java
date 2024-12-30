package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.repository.MemberTitleRepository;
import com.dongyang.dongpo.domain.member.service.TitleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

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

        titleService.addTitle(member, Title.FAILED_TO_VISIT);


        verify(memberTitleRepository).save(any());
    }

    @Test
    @DisplayName("칭호_추가_중복")
    void addTitleDuplicate() {
        Member member = mock(Member.class);
        MemberTitle memberTitle = MemberTitle.builder()
                .title(Title.FAILED_TO_VISIT)
                .build();

        when(memberTitleRepository.findByMember(member)).thenReturn(List.of(memberTitle));
        titleService.addTitle(member, Title.FAILED_TO_VISIT);

        verify(memberTitleRepository, never()).save(any());
    }

    @Test
    void getMemberTitles() {
        // given
        Member member = mock(Member.class);
        Title title1 = mock(Title.class);
        Title title2 = mock(Title.class);
        MemberTitle memberTitle1 = mock(MemberTitle.class);
        MemberTitle memberTitle2 = mock(MemberTitle.class);
        MyPageDto.TitleDto titleDto1 = mock(MyPageDto.TitleDto.class);
        MyPageDto.TitleDto titleDto2 = mock(MyPageDto.TitleDto.class);

        when(memberTitleRepository.findByMember(member)).thenReturn(List.of(memberTitle1, memberTitle2));
        when(memberTitle1.getTitle()).thenReturn(title1);
        when(memberTitle2.getTitle()).thenReturn(title2);
        when(title1.getDescription()).thenReturn("Title 1 Description");
        when(title2.getDescription()).thenReturn("Title 2 Description");
        when(title1.getAchieveCondition()).thenReturn("Condition 1");
        when(title2.getAchieveCondition()).thenReturn("Condition 2");
        when(memberTitle1.getAchieveDate()).thenReturn(LocalDateTime.now());
        when(memberTitle2.getAchieveDate()).thenReturn(LocalDateTime.now().plusDays(1));
        when(MyPageDto.toTitleDto(memberTitle1)).thenCallRealMethod();
        when(MyPageDto.toTitleDto(memberTitle2)).thenCallRealMethod();

        // when
        List<MyPageDto.TitleDto> memberTitles = titleService.getMemberTitles(member);

        // then
        assertThat(memberTitles).hasSize(2);
        verify(memberTitleRepository).findByMember(member);
    }
}