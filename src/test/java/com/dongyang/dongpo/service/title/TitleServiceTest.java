package com.dongyang.dongpo.service.title;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitleServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TitleService titleService;

    @Test
    @DisplayName("칭호_추가")
    void addTitle() {
        Member member = mock(Member.class);

        titleService.addTitle(member, Title.FAILED_TO_VISIT);

        verify(memberRepository).save(member);
    }

    @Test
    @DisplayName("칭호_추가_중복")
    void addTitleDuplicate() {
        Member member = mock(Member.class);
        MemberTitle memberTitle = MemberTitle.builder()
                .title(Title.FAILED_TO_VISIT)
                .build();

        when(member.getTitles()).thenReturn(List.of(memberTitle));

        member.addTitle(memberTitle);

        titleService.addTitle(member, Title.FAILED_TO_VISIT);

        verify(memberRepository, never()).save(member);
    }
}