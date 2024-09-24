package com.dongyang.dongpo.dto.bookmark;

import com.dongyang.dongpo.domain.store.StoreBookmark;
import com.dongyang.dongpo.dto.store.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDto {

    private Long id;
    private Long storeId;
    private String storeName;

    public static BookmarkDto toDto(StoreBookmark storeBookmark) {
        return BookmarkDto.builder()
                .id(storeBookmark.getId())
                .storeId(storeBookmark.getStore().getId())
                .storeName(storeBookmark.getStore().getName())
                .build();
    }
}
