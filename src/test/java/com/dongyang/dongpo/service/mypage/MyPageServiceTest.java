package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreSummaryDto;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.service.store.StoreReviewService;
import com.dongyang.dongpo.service.store.StoreService;
import com.dongyang.dongpo.service.title.TitleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private BookmarkService bookmarkService;

    @Mock
    private StoreReviewService storeReviewService;

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
        StoreSummaryDto store1 = mock(StoreSummaryDto.class);
        StoreSummaryDto store2 = mock(StoreSummaryDto.class);
        List<StoreSummaryDto> expectedStores = List.of(store1, store2);

        when(storeService.getMyRegisteredStores(member)).thenReturn(expectedStores);

        // when
        List<StoreSummaryDto> storeSummaryDtos = myPageService.getMyRegisteredStores(member);

        // then
        assertThat(storeSummaryDtos).isEqualTo(expectedStores);
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
        BookmarkDto bookmarkDto1 = mock(BookmarkDto.class);
        BookmarkDto bookmarkDto2 = mock(BookmarkDto.class);
        List<BookmarkDto> expectedBookmarks = List.of(bookmarkDto1, bookmarkDto2);

        when(bookmarkService.getMyBookmarks(member)).thenReturn(expectedBookmarks);

        // when
        List<BookmarkDto> bookmarkDtos = myPageService.getMyBookmarks(member);

        // then
        assertThat(bookmarkDtos).isEqualTo(expectedBookmarks);
        verify(bookmarkService).getMyBookmarks(member);
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        ReviewDto reviewDto1 = mock(ReviewDto.class);
        ReviewDto reviewDto2 = mock(ReviewDto.class);
        List<ReviewDto> expectedReviews = List.of(reviewDto1, reviewDto2);

        when(storeReviewService.getMyReviews(member)).thenReturn(expectedReviews);

        // when
        List<ReviewDto> reviewDtos = myPageService.getMyReviews(member);

        // then
        assertThat(reviewDtos).isEqualTo(expectedReviews);
        verify(storeReviewService).getMyReviews(member);
    }
}
