package com.dongyang.dongpo.admin.dto;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.domain.board.Notice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {

    private Long id;
    private String title;
    private String text;
    private String name;

    public Notice toEntity(Admin admin){
        return Notice.builder()
                .title(title)
                .text(text)
                .admin(admin)
                .build();
    }
}
