package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.member.dto.MyTitlesResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.store.dto.MyRegisteredStoresResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MyPageService {
    MyPageResponseDto getMyPageInfo(Member member);

    Page<MyRegisteredStoresResponseDto> getMyRegisteredStores(Member member, int page);

    List<MyTitlesResponseDto> getMyTitles(Member member);

    Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(Member member, int page);

    Page<MyRegisteredReviewsResponseDto> getMyReviews(Member member, int page);

    void updateMyPageInfo(Member member, MyPageUpdateRequestDto myPageUpdateRequestDto);
}
