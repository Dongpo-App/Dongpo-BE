package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberTitleRepository memberTitleRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MyPageService myPageService;

    @Test
    void getMyPageIndex() {
        // given
        Member member = mock(Member.class);
        Title title = mock(Title.class);

        when(member.getMainTitle()).thenReturn(title);
        when(title.getDescription()).thenReturn("Title Description");
        when(member.getNickname()).thenReturn("테스터");

        // when
        MyPageDto myPageIndex = myPageService.getMyPageIndex(member);

        // then
        verify(memberTitleRepository).findByMember(member);
        verify(storeRepository).findByMember(member);
        assertThat(myPageIndex.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void updateMyPageInfo() {
        // given
        Member member = mock(Member.class);
        MyPageUpdateDto myPageUpdateDto = mock(MyPageUpdateDto.class);
        MemberTitle memberTitle = mock(MemberTitle.class);

        when(member.getMainTitle()).thenReturn(Title.BASIC_TITLE);
        when(member.getNickname()).thenReturn("테스터");
        when(member.getProfilePic()).thenReturn(null);

        when(myPageUpdateDto.getNickname()).thenReturn("테스터2");
        when(myPageUpdateDto.getProfilePic()).thenReturn("https://test.com/new-profile.jpg"); // 업데이트 할 프로필 이미지
        when(myPageUpdateDto.getNewMainTitle()).thenReturn(Title.REGULAR_CUSTOMER); // 업데이트 할 메인 칭호
        when(memberTitle.getTitle()).thenReturn(Title.REGULAR_CUSTOMER);

        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(memberTitleRepository.findByMemberAndTitle(any(), any())).thenReturn(memberTitle);

        // when
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);

        // then
        verify(member).setProfilePic("https://test.com/new-profile.jpg");
        verify(member).setNickname("테스터2");
        verify(member).setMainTitle(Title.REGULAR_CUSTOMER);
    }
}
