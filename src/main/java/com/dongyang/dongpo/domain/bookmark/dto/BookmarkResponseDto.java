package com.dongyang.dongpo.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponseDto {
    private Long bookmarkCount;
    private Boolean isMemberBookmarked;
}
