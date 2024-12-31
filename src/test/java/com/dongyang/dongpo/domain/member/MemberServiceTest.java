package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.entity.Title;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
import com.dongyang.dongpo.domain.member.repository.MemberRepository;
import com.dongyang.dongpo.domain.member.repository.MemberTitleRepository;
import com.dongyang.dongpo.domain.member.service.MemberService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import com.dongyang.dongpo.common.auth.jwt.JwtService;
import com.dongyang.dongpo.common.auth.jwt.JwtUtil;
import com.dongyang.dongpo.common.fileupload.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtService jwtService;

    @Mock
    private MemberTitleRepository memberTitleRepository;

    @Mock
    private StoreService storeService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MemberService memberService;

    @Test
    void socialSave() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findOne() {
    }

    @Test
    void getMemberInfoIndex() {
    }

    @Test
    void updateMemberInfo() {
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
        memberService.updateMemberInfo(member.getEmail(), myPageUpdateDto);

        // then
        verify(member).updateMemberProfilePic("https://test.com/new-profile.jpg");
        verify(member).updateMemberNickname("테스터2");
        verify(member).updateMemberMainTitle(Title.REGULAR_CUSTOMER);
    }
}