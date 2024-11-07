package com.dongyang.dongpo.dto.bookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreBookmarkResponseDto {
    private Long bookmarkCount;
    private Boolean isMemberBookmarked;
}