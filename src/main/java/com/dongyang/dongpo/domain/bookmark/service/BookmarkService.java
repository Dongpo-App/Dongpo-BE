package com.dongyang.dongpo.domain.bookmark.service;

import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import org.springframework.data.domain.Page;

public interface BookmarkService {
    BookmarkResponseDto addBookmark(Long storeId, Member member);

    BookmarkResponseDto deleteBookmark(Long storeId, Member member);

    Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(Member member, int page);

    boolean isStoreBookmarkedByMember(Store store, Member member);

    Long getBookmarkCountByStore(Store store);
}
