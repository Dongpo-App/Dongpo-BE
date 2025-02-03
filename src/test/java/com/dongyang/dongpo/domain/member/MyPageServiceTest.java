package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.bookmark.service.BookmarkServiceImpl;
import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.member.service.MyPageService;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
import com.dongyang.dongpo.domain.member.service.MemberService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import com.dongyang.dongpo.domain.member.service.TitleService;
import com.dongyang.dongpo.domain.review.service.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyPageServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private TitleService titleService;

    @Mock
    private StoreService storeService;

    @Mock
    private BookmarkServiceImpl bookmarkService;

    @Mock
    private ReviewServiceImpl storeReviewService;

    @InjectMocks
    private MyPageService myPageService;

    @Test
    void getMyPageInfo() {
        // given
        Member member = mock(Member.class);
        MyPageResponseDto myPageResponseDto = mock(MyPageResponseDto.class);

        when(memberService.getMemberInfo(member)).thenReturn(myPageResponseDto);

        // when
        MyPageResponseDto myPageIndex = myPageService.getMyPageInfo(member);

        // then
        assertThat(myPageIndex).isEqualTo(myPageResponseDto);
        verify(memberService).getMemberInfo(member);
    }

    @Test
    void updateMyPageInfo() {
        // given
        Member member = mock(Member.class);
        MyPageUpdateRequestDto myPageUpdateRequestDto = mock(MyPageUpdateRequestDto.class);

        // when
        myPageService.updateMyPageInfo(member, myPageUpdateRequestDto);

        // then
        verify(memberService).updateMemberInfo(member, myPageUpdateRequestDto);
    }

    @Test
    void getMyRegisteredStores() {
        // given
        Member member = mock(Member.class);
        PageRequest pageRequest = PageRequest.of(0, 20);
        MyRegisteredStoresResponseDto store1 = mock(MyRegisteredStoresResponseDto.class);
        MyRegisteredStoresResponseDto store2 = mock(MyRegisteredStoresResponseDto.class);
        Page<MyRegisteredStoresResponseDto> expectedStores = new PageImpl<>(List.of(store1, store2), pageRequest, 2);

        when(storeService.getMyRegisteredStores(member, 0)).thenReturn(expectedStores);

        // when
        Page<MyRegisteredStoresResponseDto> myRegisteredStoresResponseDtos = myPageService.getMyRegisteredStores(member, 0);

        // then
        assertThat(myRegisteredStoresResponseDtos.getContent()).isEqualTo(expectedStores.getContent());
        verify(storeService).getMyRegisteredStores(member, 0);
    }

    @Test
    void getMyTitles() {
        // given
        Member member = mock(Member.class);
        MyTitlesResponseDto titleDto1 = mock(MyTitlesResponseDto.class);
        MyTitlesResponseDto titleDto2 = mock(MyTitlesResponseDto.class);
        List<MyTitlesResponseDto> expectedTitles = List.of(titleDto1, titleDto2);

        when(titleService.getMemberTitles(member)).thenReturn(expectedTitles);

        // when
        List<MyTitlesResponseDto> titleDtos = myPageService.getMyTitles(member);

        // then
        assertThat(titleDtos).isEqualTo(expectedTitles);
        verify(titleService).getMemberTitles(member);
    }

    @Test
    void getMyBookmarks() {
        // given
        Member member = mock(Member.class);
        PageRequest pageRequest = PageRequest.of(0, 20);
        MyRegisteredBookmarksResponseDto bookmark1 = mock(MyRegisteredBookmarksResponseDto.class);
        MyRegisteredBookmarksResponseDto bookmark2 = mock(MyRegisteredBookmarksResponseDto.class);
        Page<MyRegisteredBookmarksResponseDto> expectedBookmarks = new PageImpl<>(List.of(bookmark1, bookmark2), pageRequest, 2);

        when(bookmarkService.getMyBookmarks(member, 0)).thenReturn(expectedBookmarks);

        // when
        Page<MyRegisteredBookmarksResponseDto> myRegisteredBookmarksResponseDtos = myPageService.getMyBookmarks(member, 0);

        // then
        assertThat(myRegisteredBookmarksResponseDtos.getContent()).isEqualTo(expectedBookmarks.getContent());
        verify(bookmarkService).getMyBookmarks(member, 0);
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        Page<MyRegisteredReviewsResponseDto> expectedPage = mock(Page.class);

        when(storeReviewService.getMyReviews(member, 0)).thenReturn(expectedPage);

        // when
        Page<MyRegisteredReviewsResponseDto> result = myPageService.getMyReviews(member, 0);

        // then
        assertThat(result).isEqualTo(expectedPage);
        verify(storeReviewService).getMyReviews(member, 0);
    }
}
