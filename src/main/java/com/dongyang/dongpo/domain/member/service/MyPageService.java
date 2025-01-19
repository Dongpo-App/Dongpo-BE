package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.bookmark.dto.BookmarkDto;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.ReviewDto;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
import com.dongyang.dongpo.domain.store.service.StoreReviewService;
import com.dongyang.dongpo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final StoreService storeService;
    private final BookmarkService bookmarkService;
    private final StoreReviewService storeReviewService;
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

    public List<BookmarkDto> getMyBookmarks(Member member) {
        return bookmarkService.getMyBookmarks(member);
    }

    public List<ReviewDto> getMyReviews(Member member) {
        return storeReviewService.getMyReviews(member); // TODO: 페이징 구현
    }

    public void updateMyPageInfo(String email, MyPageUpdateDto myPageUpdateDto) {
        memberService.updateMemberInfo(email, myPageUpdateDto);
    }
}
