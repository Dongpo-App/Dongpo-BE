package com.dongyang.dongpo.domain.bookmark.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkResponseDto;
import com.dongyang.dongpo.domain.bookmark.repository.BookmarkRepository;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.Bookmark;
import com.dongyang.dongpo.domain.store.repository.ReadOnlyStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final static int DEFAULT_PAGE_SIZE = 20;

    private final BookmarkRepository bookmarkRepository;
    private final ReadOnlyStoreRepository storeRepository;

    // 북마크 추가
    @Transactional
    public BookmarkResponseDto addBookmark(final Long storeId, final Member member) {
        Store store = getStoreById(storeId);

        if (isStoreBookmarkedByMember(store, member))
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXISTS);

        bookmarkRepository.save(Bookmark.builder()
                .store(store)
                .member(member)
                .build());

        log.info("Member {} added Bookmark - Store Id : {}", member.getEmail(), storeId);
        return BookmarkResponseDto.builder()
                .isMemberBookmarked(true)
                .bookmarkCount(getBookmarkCountByStore(store))
                .build();
    }

    // 북마크 제거
    @Transactional
    public BookmarkResponseDto deleteBookmark(final Long storeId, final Member member) {
        Store store = getStoreById(storeId);

        bookmarkRepository.delete(findByStoreAndMember(store, member));
        log.info("Member {} deleted Bookmark - StoreId : {}", member.getEmail(), storeId);
        return BookmarkResponseDto.builder()
                .isMemberBookmarked(false)
                .bookmarkCount(getBookmarkCountByStore(store))
                .build();
    }

    // 내 북마크 조회
    public Page<MyRegisteredBookmarksResponseDto> getMyBookmarks(final Member member, final int page) {
        Page<Bookmark> storeBookmarks = bookmarkRepository.findByMember(member, PageRequest.of(page, DEFAULT_PAGE_SIZE));

        if (storeBookmarks.isEmpty())
            throw new CustomException(ErrorCode.BOOKMARKS_REGISTERED_BY_MEMBER_NOT_FOUND);

        return new PageImpl<>(storeBookmarks
                .stream()
                .map(Bookmark::toMyRegisteredBookmarksResponse)
                .toList(), storeBookmarks.getPageable(), storeBookmarks.getTotalElements());
    }

    // 북마크 여부 확인
    public boolean isStoreBookmarkedByMember(final Store store, final Member member) {
        return bookmarkRepository.existsByStoreAndMember(store, member);
    }

    // 점포에 대한 북마크 수 조회
    public Long getBookmarkCountByStore(final Store store) {
        return bookmarkRepository.countByStore(store);
    }

    // 점포 조회
    private Store getStoreById(final Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    // 북마크 조회
    private Bookmark findByStoreAndMember(final Store store, final Member member) {
        return bookmarkRepository.findByStoreAndMember(store, member)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));
    }
}
