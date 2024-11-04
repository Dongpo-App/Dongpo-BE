package com.dongyang.dongpo.domain.board;

import com.dongyang.dongpo.dto.notice.PicDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board_notice_pic")
public class NoticePic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Column(length = 128)
    private String picUrl;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registerDate= LocalDateTime.now();

    public PicDto toResponse(){
        return PicDto.builder()
                .id(id)
                .url(picUrl)
                .build();
    }
}
