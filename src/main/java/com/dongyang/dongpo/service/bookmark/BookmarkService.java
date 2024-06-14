package com.dongyang.dongpo.service.bookmark;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.StoreBookmark;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.bookmark.BookmarkDto;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.exception.store.StoreNotFoundException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.bookmark.BookmarkRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void addBookmark(String accessToken, Long storeId) throws Exception {
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);

        StoreBookmark bookmark = StoreBookmark.builder()
                .store(store)
                .member(member)
                .build();

        bookmarkRepository.save(bookmark);

        log.info("Member Id : {} is Add Bookmark Store Id : {}", member.getId(), storeId);
    }

    public List<BookmarkDto> bookmarkList(String accessToken) throws Exception {
        String email = jwtTokenProvider.parseClaims(accessToken).getSubject();
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
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
}
