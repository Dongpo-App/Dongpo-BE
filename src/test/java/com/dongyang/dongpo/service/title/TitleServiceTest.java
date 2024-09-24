package com.dongyang.dongpo.service.title;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
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
}