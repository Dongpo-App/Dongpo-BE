package com.dongyang.dongpo.domain.bookmark.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkDto;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkResponseDto;
import com.dongyang.dongpo.domain.bookmark.repository.BookmarkRepository;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.StoreBookmark;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public BookmarkResponseDto addBookmark(final Member member, final Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreBookmark bookmark = StoreBookmark.builder()
                .store(store)
                .member(member)
                .build();

        try {
            bookmarkRepository.save(bookmark);
        } catch (DataIntegrityViolationException ignore) { // 해당 점포에 대한 사용자의 북마크가 이미 존재할 경우
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        log.info("Member {} added Bookmark - Store Id : {}", member.getEmail(), storeId);
        return BookmarkResponseDto.builder()
                .isMemberBookmarked(true)
                .bookmarkCount(getBookmarkCountByStore(store))
                .build();
    }

    public List<BookmarkDto> getMyBookmarks(Member member) {
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();
        bookmarkRepository.findByMember(member).forEach(bookmark -> {
            bookmarkDtos.add(BookmarkDto.toDto(bookmark));
        });
        return bookmarkDtos;
    }

    @Transactional
    public BookmarkResponseDto deleteBookmark(Long storeId, Member member) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        StoreBookmark bookmark = bookmarkRepository.findByStoreAndMember(store, member)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
        log.info("Member {} deleted Bookmark - StoreId : {}", member.getEmail(), storeId);
        return BookmarkResponseDto.builder()
                .isMemberBookmarked(false)
                .bookmarkCount(getBookmarkCountByStore(store))
                .build();
    }

    public boolean isStoreBookmarkedByMember(Store store, Member member) {
        return bookmarkRepository.existsByStoreAndMember(store, member);
    }

    public Long getBookmarkCountByStore(final Store store) {
        return bookmarkRepository.countByStore(store);
    }
}
