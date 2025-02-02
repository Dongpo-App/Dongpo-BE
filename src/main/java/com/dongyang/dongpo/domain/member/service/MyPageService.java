package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
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

    public MyPageDto getMyPageIndex(Member member) {
        return memberService.getMemberInfoIndex(member);
    }

    public List<MyRegisteredStoresResponseDto> getMyRegisteredStores(Member member) {
        return storeService.getMyRegisteredStores(member);
    }

    public List<MyPageDto.TitleDto> getMyTitles(Member member) {
        return titleService.getMemberTitles(member);
    }

    public Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(Member member, int page) {
        return bookmarkService.getMyBookmarks(member, page);
    }

    public Page<MyRegisteredReviewsResponseDto> getMyReviews(final Member member, final int page) {
        return reviewService.getMyReviews(member, page);
    }

    public void updateMyPageInfo(String email, MyPageUpdateDto myPageUpdateDto) {
        memberService.updateMemberInfo(email, myPageUpdateDto);
    }
}
