package com.dongyang.dongpo.domain.bookmark.entity;

import com.dongyang.dongpo.domain.bookmark.dto.MyRegisteredBookmarksResponseDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.entity.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_bookmark", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "store_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @NotNull
    private Store store;

    @CreatedDate
    @Column(name = "bookmark_date")
    @NotNull
    private LocalDateTime bookmarkDate;

    public MyRegisteredBookmarksResponseDto toMyRegisteredBookmarksResponse() {
        return MyRegisteredBookmarksResponseDto.builder()
                .id(this.id)
                .storeId(this.store.getId())
                .storeName(this.store.getName())
                .bookmarkDate(this.bookmarkDate)
                .build();
    }
}
