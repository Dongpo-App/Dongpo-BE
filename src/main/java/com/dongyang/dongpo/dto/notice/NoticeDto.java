package com.dongyang.dongpo.dto.notice;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.domain.board.Notice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
