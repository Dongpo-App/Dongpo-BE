package com.dongyang.dongpo.dto.bookmark;

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
    private StoreDto store;
}
