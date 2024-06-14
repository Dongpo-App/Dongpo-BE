package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.PK.StoreBookmarkId;
import com.dongyang.dongpo.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_bookmark")
public class StoreBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime bookmarkDate = LocalDateTime.now();
}
