package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreBookmark;
import com.dongyang.dongpo.domain.store.StoreReview;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreIndexDto;
import com.dongyang.dongpo.repository.bookmark.BookmarkRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import com.dongyang.dongpo.repository.store.StoreReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
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

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private StoreReviewRepository storeReviewRepository;

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
        verify(storeRepository).countByMember(member);
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

    @Test
    void getMyRegisteredStores() {
        // given
        Member member = mock(Member.class);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        StoreIndexDto storeIndexDto1 = mock(StoreIndexDto.class);
        StoreIndexDto storeIndexDto2 = mock(StoreIndexDto.class);

        when(storeRepository.findByMember(member)).thenReturn(List.of(store1, store2));
        when(store1.toIndexResponse()).thenReturn(storeIndexDto1);
        when(store2.toIndexResponse()).thenReturn(storeIndexDto2);

        // when
        List<StoreIndexDto> storeIndexDtos = myPageService.getMyRegisteredStores(member);

        // then
        assertThat(storeIndexDtos).hasSize(2);
        assertThat(storeIndexDtos).contains(storeIndexDto1, storeIndexDto2);
        verify(storeRepository).findByMember(member);
        verify(store1).toIndexResponse();
        verify(store2).toIndexResponse();
    }

    @Test
    void getMyTitles() {
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
        List<MyPageDto.TitleDto> myTitles = myPageService.getMyTitles(member);

        // then
        assertThat(myTitles).hasSize(2);
        verify(memberTitleRepository).findByMember(member);
    }

    @Test
    void getMyBookmarks() {
        // given
        Member member = mock(Member.class);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        StoreBookmark bookmark1 = mock(StoreBookmark.class);
        StoreBookmark bookmark2 = mock(StoreBookmark.class);
        LocalDateTime bookmarkDate1 = LocalDateTime.now();
        LocalDateTime bookmarkDate2 = LocalDateTime.now().plusDays(1);

        when(bookmarkRepository.findByMember(member)).thenReturn(List.of(bookmark1, bookmark2));
        when(bookmark1.getStore()).thenReturn(store1);
        when(bookmark2.getStore()).thenReturn(store2);
        when(bookmark1.getBookmarkDate()).thenReturn(bookmarkDate1);
        when(bookmark2.getBookmarkDate()).thenReturn(bookmarkDate2);
        when(BookmarkDto.toDto(bookmark1)).thenCallRealMethod();
        when(BookmarkDto.toDto(bookmark2)).thenCallRealMethod();

        // when
        List<BookmarkDto> bookmarkDtos = myPageService.getMyBookmarks(member);

        // then
        assertThat(bookmarkDtos).hasSize(2);
        verify(bookmarkRepository).findByMember(member);
    }

    @Test
    void getMyReviews() {
        // given
        Member member = mock(Member.class);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        StoreReview review1 = mock(StoreReview.class);
        StoreReview review2 = mock(StoreReview.class);
        ReviewDto reviewDto1 = mock(ReviewDto.class);
        ReviewDto reviewDto2 = mock(ReviewDto.class);
        Integer reportCount1 = 5;
        Integer reportCount2 = 3;

        when(storeReviewRepository.findByMember(member)).thenReturn(List.of(review1, review2));
        when(review1.getMember()).thenReturn(member);
        when(review2.getMember()).thenReturn(member);
        when(review1.getStore()).thenReturn(store1);
        when(review2.getStore()).thenReturn(store2);
        when(review1.getReportCount()).thenReturn(reportCount1);
        when(review2.getReportCount()).thenReturn(reportCount2);
        when(ReviewDto.toDto(review1)).thenCallRealMethod();
        when(ReviewDto.toDto(review2)).thenCallRealMethod();

        // when
        List<ReviewDto> reviewDtos = myPageService.getMyReviews(member);

        // then
        assertThat(reviewDtos).hasSize(2);
        verify(storeReviewRepository).findByMember(member);
    }
}
