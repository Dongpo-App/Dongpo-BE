package com.dongyang.dongpo.domain.bookmark;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkResponseDto;
import com.dongyang.dongpo.domain.bookmark.service.BookmarkServiceImpl;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.Bookmark;
import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.bookmark.repository.BookmarkRepository;
import com.dongyang.dongpo.domain.store.repository.ReadOnlyStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private ReadOnlyStoreRepository storeRepository;

    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    @Test
    void addBookmark() {
        // given
        Long storeId = 1L;
        Member member = mock(Member.class);
        Store store = mock(Store.class);
        Bookmark bookmark = Bookmark.builder().store(store).member(member).build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);
        when(bookmarkRepository.countByStore(store)).thenReturn(1L);

        // when
        BookmarkResponseDto response = bookmarkService.addBookmark(storeId, member);

        // then
        assertThat(response.getIsMemberBookmarked()).isTrue();
        assertThat(response.getBookmarkCount()).isEqualTo(1L);
        verify(bookmarkRepository).save(any(Bookmark.class));
    }

    @Test
    void addBookmark_AlreadyExists() {
        // given
        Long storeId = 1L;
        Member member = mock(Member.class);
        Store store = mock(Store.class);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.existsByStoreAndMember(store, member)).thenReturn(true);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> bookmarkService.addBookmark(storeId, member));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOOKMARK_ALREADY_EXISTS);
    }

    @Test
    void deleteBookmark() {
        // given
        Long storeId = 1L;
        Member member = mock(Member.class);
        Store store = mock(Store.class);
        Bookmark bookmark = Bookmark.builder().store(store).member(member).build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.findByStoreAndMember(store, member)).thenReturn(Optional.of(bookmark));
        when(bookmarkRepository.countByStore(store)).thenReturn(0L);

        // when
        BookmarkResponseDto response = bookmarkService.deleteBookmark(storeId, member);

        // then
        assertThat(response.getIsMemberBookmarked()).isFalse();
        assertThat(response.getBookmarkCount()).isEqualTo(0L);
        verify(bookmarkRepository).delete(bookmark);
    }

    @Test
    void deleteBookmark_NotFound() {
        // given
        Long storeId = 1L;
        Member member = mock(Member.class);
        Store store = mock(Store.class);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(bookmarkRepository.findByStoreAndMember(store, member)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> bookmarkService.deleteBookmark(storeId, member));

        // then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOOKMARK_NOT_FOUND);
    }

    @Test
    void getMyBookmarks() {
        // given
        Member member = mock(Member.class);
        PageRequest pageRequest = PageRequest.of(0, 20);
        Bookmark bookmark = mock(Bookmark.class);
        Page<Bookmark> storeBookmarks = new PageImpl<>(List.of(bookmark));

        when(bookmarkRepository.findByMember(member, pageRequest)).thenReturn(storeBookmarks);

        // when
        Page<MyRegisteredBookmarksResponseDto> response = bookmarkService.getMyBookmarks(member, 0);

        // then
        assertThat(response.getContent()).hasSize(1);
        verify(bookmarkRepository).findByMember(member, pageRequest);
    }

    @Test
    void isStoreBookmarkedByMember() {
        // given
        Store store = mock(Store.class);
        Member member = mock(Member.class);

        when(bookmarkRepository.existsByStoreAndMember(store, member)).thenReturn(true);

        // when
        boolean isBookmarked = bookmarkService.isStoreBookmarkedByMember(store, member);

        // then
        assertThat(isBookmarked).isTrue();
        verify(bookmarkRepository).existsByStoreAndMember(store, member);
    }

    @Test
    void getBookmarkCountByStore() {
        // given
        Store store = mock(Store.class);

        when(bookmarkRepository.countByStore(store)).thenReturn(5L);

        // when
        Long count = bookmarkService.getBookmarkCountByStore(store);

        // then
        assertThat(count).isEqualTo(5L);
        verify(bookmarkRepository).countByStore(store);
    }
}