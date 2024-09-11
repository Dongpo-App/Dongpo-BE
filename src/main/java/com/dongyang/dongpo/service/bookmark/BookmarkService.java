package com.dongyang.dongpo.service.bookmark;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.StoreBookmark;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
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
    public void addBookmark(Member member, Long storeId) throws Exception {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

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

        for (StoreBookmark storeBookmark : storeBookmarks){
            bookmarkDtos.add(BookmarkDto.builder()
                    .id(storeBookmark.getId())
                    .store(storeBookmark.getStore().toResponse())
                    .build());
        }

        return bookmarkDtos;
    }

    @Transactional
    public void deleteBookmark(Long id, Member member) throws Exception {
        StoreBookmark bookmark = bookmarkRepository.findById(id).orElseThrow(StoreNotFoundException::new);

        bookmarkRepository.delete(bookmark);
        log.info("Member Id : {} is Delete Bookmark Id : {}", member.getId(), id);
    }
}
