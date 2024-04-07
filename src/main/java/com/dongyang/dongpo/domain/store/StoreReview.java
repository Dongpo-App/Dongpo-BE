package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_review")
public class StoreReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(optional = false)
    @JoinColumn(name = "review_writer")
    private Member reviewWriter;

    private Integer reviewStar;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @Column(length = 128)
    private String reviewPic;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registerDate;

    @Column(length = 24)
    private String registerIp;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus = ReviewStatus.VISIBLE;

    private Integer reportCount = 0;

    public enum ReviewStatus {
        VISIBLE, HIDDEN, DELETED
    }
}
