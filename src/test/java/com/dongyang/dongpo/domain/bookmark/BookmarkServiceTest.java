package com.dongyang.dongpo.domain.bookmark;

import com.dongyang.dongpo.domain.bookmark.service.BookmarkService;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.bookmark.entity.StoreBookmark;
import com.dongyang.dongpo.domain.bookmark.dto.BookmarkDto;
import com.dongyang.dongpo.domain.bookmark.repository.BookmarkRepository;
import com.dongyang.dongpo.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @Test
    void addBookmark() {
    }

    @Test
    void getMyBookmarks() {
        // given
        Member member = mock(Member.class);
        Store store1 = mock(Store.class);
        Store store2 = mock(Store.class);
        StoreBookmark bookmark1 = mock(StoreBookmark.class);
        StoreBookmark bookmark2 = mock(StoreBookmark.class);
        LocalDateTime bookmarkDate1 = LocalDateTime.now();
        LocalDateTime bookmarkDate2 = LocalDateTime.now().plusDays(1);

        when(bookmarkRepository.findByMember(member)).thenReturn(List.of(bookmark1, bookmark2));
        when(bookmark1.getStore()).thenReturn(store1);
        when(bookmark2.getStore()).thenReturn(store2);
        when(bookmark1.getBookmarkDate()).thenReturn(bookmarkDate1);
        when(bookmark2.getBookmarkDate()).thenReturn(bookmarkDate2);
        when(BookmarkDto.toDto(bookmark1)).thenCallRealMethod();
        when(BookmarkDto.toDto(bookmark2)).thenCallRealMethod();

        // when
        List<BookmarkDto> bookmarkDtos = bookmarkService.getMyBookmarks(member);

        // then
        assertThat(bookmarkDtos).hasSize(2);
        verify(bookmarkRepository).findByMember(member);
    }

    @Test
    void deleteBookmark() {
    }
}