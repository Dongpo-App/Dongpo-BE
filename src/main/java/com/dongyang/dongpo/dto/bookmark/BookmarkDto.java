package com.dongyang.dongpo.dto.bookmark;

import com.dongyang.dongpo.domain.store.StoreBookmark;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookmarkDto {

    private Long id;
    private Long storeId;
    private String storeName;
    private LocalDateTime bookmarkDate;

    public static BookmarkDto toDto(StoreBookmark storeBookmark) {
        return BookmarkDto.builder()
                .id(storeBookmark.getId())
                .storeId(storeBookmark.getStore().getId())
                .storeName(storeBookmark.getStore().getName())
                .bookmarkDate(storeBookmark.getBookmarkDate())
                .build();
    }
}
