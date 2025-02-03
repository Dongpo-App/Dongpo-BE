package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.auth.dto.UserInfo;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.enums.Title;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.member.repository.MemberRepository;
import com.dongyang.dongpo.domain.member.service.MemberService;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import com.dongyang.dongpo.common.fileupload.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private TitleService titleService;

    @Mock
    private StoreService storeService;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private MemberService memberService;

    @Test
    void registerNewMember() {
        UserInfo userInfo = mock(UserInfo.class);
        Member member = mock(Member.class);

        when(userInfo.toMemberEntity()).thenReturn(member);
        when(member.getMainTitle()).thenReturn(Title.BASIC_TITLE);

        Member result = memberService.registerNewMember(userInfo);

        assertThat(result).isEqualTo(member);
        verify(memberRepository).save(member);
        verify(titleService).addTitle(member, member.getMainTitle());
    }

    @Test
    void findAll() {
        List<Member> members = List.of(mock(Member.class), mock(Member.class));

        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.findAll();

        assertThat(result).isEqualTo(members);
        verify(memberRepository).findAll();
    }

    @Test
    void findById() {
        Member member = mock(Member.class);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Member result = memberService.findById(1L);

        assertThat(result).isEqualTo(member);
        verify(memberRepository).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findById(1L));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findById(1L);
    }

    @Test
    void findByEmail() {
        Member member = mock(Member.class);

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));

        Member result = memberService.findByEmail("test@example.com");

        assertThat(result).isEqualTo(member);
        verify(memberRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_NotFound() {
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> memberService.findByEmail("test@example.com"));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
        verify(memberRepository).findByEmail("test@example.com");
    }

    @Test
    void setMemberStatusLeave() {
        Member member = mock(Member.class);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        memberService.setMemberStatusLeave(member);

        verify(member).setMemberStatusLeave();
    }

    @Test
    void validateMemberExistence_AlreadyExists() {
        String email = "test@example.com";
        String socialId = "social123";
        Member member = mock(Member.class);

        when(memberRepository.findBySocialId(socialId)).thenReturn(Optional.of(member));

        boolean result = memberService.validateMemberExistence(email, socialId);

        assertThat(result).isTrue();
        verify(memberRepository).findBySocialId(socialId);
    }

    @Test
    void validateMemberExistence_EmailDuplicated() {
        String email = "test@example.com";
        String socialId = "social123";

        when(memberRepository.findBySocialId(socialId)).thenReturn(Optional.empty());
        when(memberRepository.existsByEmail(email)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> memberService.validateMemberExistence(email, socialId));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_EMAIL_DUPLICATED);
        verify(memberRepository).findBySocialId(socialId);
        verify(memberRepository).existsByEmail(email);
    }

    @Test
    void getMemberInfo() {
        Member member = mock(Member.class);
        when(member.getNickname()).thenReturn("nickname");
        when(member.getProfilePic()).thenReturn("profilePic");
        when(member.getMainTitle()).thenReturn(Title.BASIC_TITLE);
        when(storeService.getMyRegisteredStoreCount(member)).thenReturn(5L);
        when(titleService.getMemberTitlesCount(member)).thenReturn(3L);

        MyPageResponseDto result = memberService.getMemberInfo(member);

        assertThat(result.getNickname()).isEqualTo("nickname");
        assertThat(result.getProfilePic()).isEqualTo("profilePic");
        assertThat(result.getMainTitle()).isEqualTo(Title.BASIC_TITLE.getDescription());
        assertThat(result.getRegisterCount()).isEqualTo(5);
        assertThat(result.getTitleCount()).isEqualTo(3);
    }

    @Test
    void updateMemberInfo() {
        Member member = mock(Member.class);
        MyPageUpdateRequestDto myPageUpdateRequestDto = mock(MyPageUpdateRequestDto.class);
        MemberTitle memberTitle = mock(MemberTitle.class);

        when(member.getMainTitle()).thenReturn(Title.BASIC_TITLE);
        when(member.getNickname()).thenReturn("테스터");
        when(member.getProfilePic()).thenReturn(null);

        when(myPageUpdateRequestDto.getNickname()).thenReturn("테스터2");
        when(myPageUpdateRequestDto.getProfilePic()).thenReturn("https://test.com/new-profile.jpg");
        when(myPageUpdateRequestDto.getNewMainTitle()).thenReturn(Title.REGULAR_CUSTOMER);
        when(memberTitle.getTitle()).thenReturn(Title.REGULAR_CUSTOMER);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(titleService.findByMemberAndTitle(any(), any())).thenReturn(memberTitle);

        memberService.updateMemberInfo(member, myPageUpdateRequestDto);

        verify(member).updateMemberProfilePic("https://test.com/new-profile.jpg");
        verify(member).updateMemberNickname("테스터2");
        verify(member).updateMemberMainTitle(Title.REGULAR_CUSTOMER);
    }
}