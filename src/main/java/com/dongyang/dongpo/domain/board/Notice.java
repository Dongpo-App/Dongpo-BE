package com.dongyang.dongpo.domain.board;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.dto.notice.NoticeDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board_notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;

    @Column(length = 64)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    public NoticeDto toResponse() {
        return NoticeDto.builder()
                .id(id)
                .title(title)
                .text(text)
                .name(admin.getName())
                .build();
    }
}
