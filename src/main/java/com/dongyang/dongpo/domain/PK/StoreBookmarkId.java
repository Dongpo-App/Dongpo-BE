package com.dongyang.dongpo.domain.PK;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class StoreBookmarkId implements Serializable {
    private Long memberId;
    private Long storeId;
}
