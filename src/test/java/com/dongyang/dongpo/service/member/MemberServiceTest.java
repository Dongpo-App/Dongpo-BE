package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.s3.S3Service;
import com.dongyang.dongpo.service.store.StoreService;
import com.dongyang.dongpo.service.token.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenService tokenService;

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