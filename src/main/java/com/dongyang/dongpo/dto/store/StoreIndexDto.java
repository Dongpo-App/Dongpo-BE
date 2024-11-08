package com.dongyang.dongpo.dto.store;

import com.dongyang.dongpo.domain.store.Store;
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
public class StoreIndexDto {
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
