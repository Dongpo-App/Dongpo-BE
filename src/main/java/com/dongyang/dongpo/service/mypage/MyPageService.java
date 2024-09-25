package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreIndexDto;
import com.dongyang.dongpo.service.bookmark.BookmarkService;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.service.store.StoreReviewService;
import com.dongyang.dongpo.service.store.StoreService;
import com.dongyang.dongpo.service.title.TitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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

    public List<StoreIndexDto> getMyRegisteredStores(Member member) {
        return storeService.getMyRegisteredStores(member);
    }

    public List<MyPageDto.TitleDto> getMyTitles(Member member) {
        return titleService.getMemberTitles(member);
    }

    public List<BookmarkDto> getMyBookmarks(Member member) {
        return bookmarkService.getMyBookmarks(member);
    }

    public List<ReviewDto> getMyReviews(Member member) {
        return storeReviewService.getMyReviews(member);
    }

    public void updateMyPageInfo(String email, MyPageUpdateDto myPageUpdateDto) {
        memberService.updateMemberInfo(email, myPageUpdateDto);
    }
}
