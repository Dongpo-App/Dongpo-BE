package com.dongyang.dongpo.admin.dto;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.domain.board.Notice;
import lombok.Data;

@Data
public class NoticeDto {

    private String title;
    private String content;

    public Notice toEntity(Admin admin){
        return Notice.builder()
                .title(title)
                .text(content)
                .admin(admin)
                .build();
    }
}
