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
public class MyPageServiceImpl implements MyPageService {

    private final StoreService storeService;
    private final BookmarkService bookmarkService;
    private final ReviewService reviewService;
    private final TitleService titleService;
    private final MemberService memberService;

    // 사용자 정보 조회
    @Override
    public MyPageResponseDto getMyPageInfo(final Member member) {
        return memberService.getMemberInfo(member);
    }

    // 사용자 등록 점포 조회
    @Override
    public Page<MyRegisteredStoresResponseDto> getMyRegisteredStores(final Member member, final int page) {
        return storeService.getMyRegisteredStores(member, page);
    }

    // 사용자 칭호 조회
    @Override
    public List<MyTitlesResponseDto> getMyTitles(final Member member) {
        return titleService.getMemberTitles(member);
    }

    // 사용자 북마크 조회
    @Override
    public Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(final Member member, final int page) {
        return bookmarkService.getMyBookmarks(member, page);
    }

    // 사용자 등록 리뷰 조회
    @Override
    public Page<MyRegisteredReviewsResponseDto> getMyReviews(final Member member, final int page) {
        return reviewService.getMyReviews(member, page);
    }

    // 사용자 정보 수정
    @Override
    public void updateMyPageInfo(final Member member, final MyPageUpdateRequestDto myPageUpdateRequestDto) {
        memberService.updateMemberInfo(member, myPageUpdateRequestDto);
    }
}
