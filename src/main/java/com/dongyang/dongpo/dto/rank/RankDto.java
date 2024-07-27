package com.dongyang.dongpo.dto.rank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankDto {

    private String nickname;
    private Long count;

    public static RankDto toDto(Object[] obj) {
        return new RankDto((String) obj[0], (Long) obj[1]);
    }
}
