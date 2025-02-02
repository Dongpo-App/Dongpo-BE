package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.bookmark.service.BookmarkServiceImpl;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.member.service.MyPageService;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
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
    void getMyPageIndex() {
        // given
        Member member = mock(Member.class);
        MyPageDto myPageDto = mock(MyPageDto.class);

        when(memberService.getMemberInfoIndex(member)).thenReturn(myPageDto);

        // when
        MyPageDto myPageIndex = myPageService.getMyPageIndex(member);

        // then
        assertThat(myPageIndex).isEqualTo(myPageDto);
        verify(memberService).getMemberInfoIndex(member);
    }

    @Test
    void updateMyPageInfo() {
        // given
        Member member = mock(Member.class);
        MyPageUpdateDto myPageUpdateDto = mock(MyPageUpdateDto.class);

        // when
        myPageService.updateMyPageInfo(member.getEmail(), myPageUpdateDto);

        // then
        verify(memberService).updateMemberInfo(member.getEmail(), myPageUpdateDto);
    }

    @Test
    void getMyRegisteredStores() {
        // given
        Member member = mock(Member.class);
        MyRegisteredStoresResponseDto store1 = mock(MyRegisteredStoresResponseDto.class);
        MyRegisteredStoresResponseDto store2 = mock(MyRegisteredStoresResponseDto.class);
        List<MyRegisteredStoresResponseDto> expectedStores = List.of(store1, store2);

        when(storeService.getMyRegisteredStores(member)).thenReturn(expectedStores);

        // when
        List<MyRegisteredStoresResponseDto> myRegisteredStoresResponseDtos = myPageService.getMyRegisteredStores(member);

        // then
        assertThat(myRegisteredStoresResponseDtos).isEqualTo(expectedStores);
        verify(storeService).getMyRegisteredStores(member);
    }

    @Test
    void getMyTitles() {
        // given
        Member member = mock(Member.class);
        MyPageDto.TitleDto titleDto1 = mock(MyPageDto.TitleDto.class);
        MyPageDto.TitleDto titleDto2 = mock(MyPageDto.TitleDto.class);
        List<MyPageDto.TitleDto> expectedTitleDtos = List.of(titleDto1, titleDto2);

        when(titleService.getMemberTitles(member)).thenReturn(expectedTitleDtos);

        // when
        List<MyPageDto.TitleDto> titleDtos = myPageService.getMyTitles(member);

        // then
        assertThat(titleDtos).isEqualTo(expectedTitleDtos);
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
