package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.review.service.ReviewService;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
import com.dongyang.dongpo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final StoreService storeService;
    private final BookmarkService bookmarkService;
    private final ReviewService reviewService;
    private final TitleService titleService;
    private final MemberService memberService;

    public MyPageResponseDto getMyPageInfo(final Member member) {
        return memberService.getMemberInfo(member);
    }

    public Page<MyRegisteredStoresResponseDto> getMyRegisteredStores(final Member member, final int page) {
        return storeService.getMyRegisteredStores(member, page);
    }

    public List<MyTitlesResponseDto> getMyTitles(final Member member) {
        return titleService.getMemberTitles(member);
    }

    public Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(final Member member, final int page) {
        return bookmarkService.getMyBookmarks(member, page);
    }

    public Page<MyRegisteredReviewsResponseDto> getMyReviews(final Member member, final int page) {
        return reviewService.getMyReviews(member, page);
    }

    public void updateMyPageInfo(final Member member, final MyPageUpdateRequestDto myPageUpdateRequestDto) {
        memberService.updateMemberInfo(member, myPageUpdateRequestDto);
    }
}
