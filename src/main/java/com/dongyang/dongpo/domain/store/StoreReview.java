package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.store.ReviewDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_review")
public class StoreReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Store store;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    private Integer reviewStar;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(length = 128)
    private String reviewPic;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(length = 24)
    private String registerIp;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.VISIBLE;

    @Builder.Default
    private Integer reportCount = 0;

    public enum ReviewStatus {
        VISIBLE, HIDDEN, DELETED
    }

    public ReviewDto toResponse(){
        return ReviewDto.builder()
                .id(id)
                .registerDate(registerDate)
                .reviewStar(reviewStar)
                .text(text)
                .memberId(member.getId())
                .storeId(store.getId())
                .reviewPic(reviewPic)
                .build();
    }

    public StoreReview addReport(){
        this.reportCount++;
        return this;
    }
}
