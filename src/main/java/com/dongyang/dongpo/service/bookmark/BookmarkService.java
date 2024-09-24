package com.dongyang.dongpo.service.bookmark;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.StoreBookmark;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.bookmark.BookmarkRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        bookmarkRepository.save(bookmark);

        log.info("Member Id : {} is Add Bookmark Store Id : {}", member.getId(), storeId);
    }

    public List<BookmarkDto> bookmarkList(Member member) {
        List<StoreBookmark> storeBookmarks = bookmarkRepository.findByMemberId(member.getId());
        List<BookmarkDto> bookmarkDtos = new ArrayList<>();

        for (StoreBookmark storeBookmark : storeBookmarks)
            bookmarkDtos.add(BookmarkDto.toDto(storeBookmark));

        return bookmarkDtos;
    }

    @Transactional
    public void deleteBookmark(Long id, Member member) {
        StoreBookmark bookmark = bookmarkRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
        log.info("Member Id : {} is Delete Bookmark Id : {}", member.getId(), id);
    }
}
