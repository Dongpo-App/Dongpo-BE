package com.dongyang.dongpo.service.bookmark;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreBookmark;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.bookmark.BookmarkRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
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
    public void addBookmark(Member member, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        StoreBookmark bookmark = StoreBookmark.builder()
                .store(store)
                .member(member)
                .build();

        try {
            bookmarkRepository.save(bookmark);
        } catch (DataIntegrityViolationException ignore) {
            throw new CustomException(ErrorCode.BOOKMARK_ALREADY_EXISTS);
        }

        log.info("Member Id : {} is Add Bookmark Store Id : {}", member.getId(), storeId);
    }

    public List<BookmarkDto> getMyBookmarks(Member member) {
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();
        bookmarkRepository.findByMember(member).forEach(bookmark -> {
            bookmarkDtos.add(BookmarkDto.toDto(bookmark));
        });
        return bookmarkDtos;
    }

    @Transactional
    public void deleteBookmark(Long storeId, Member member) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
        StoreBookmark bookmark = bookmarkRepository.findByStoreAndMember(store, member)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
        log.info("Member {} deleted Bookmark : storeId -> {}", member.getEmail(), storeId);
    }

    public boolean isStoreBookmarkedByMember(Store store, Member member) {
        return bookmarkRepository.existsByStoreAndMember(store, member);
    }

    public Long getBookmarkCountByStore(final Store store) {
        return bookmarkRepository.countByStore(store);
    }
}
