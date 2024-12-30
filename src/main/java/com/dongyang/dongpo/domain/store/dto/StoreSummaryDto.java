package com.dongyang.dongpo.domain.store.dto;

import com.dongyang.dongpo.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSummaryDto {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private LocalDateTime registerDate;
    private Store.StoreStatus status;
    private OpenPossibility openPossibility;
    private Boolean isBookmarked;
    private List<String> reviewPics;
}
