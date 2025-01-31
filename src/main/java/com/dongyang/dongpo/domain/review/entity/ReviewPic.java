package com.dongyang.dongpo.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_review_pic")
@EntityListeners(AuditingEntityListener.class)
public class ReviewPic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_review_id")
    private Review review;

    @Column(length = 128)
    private String picUrl;

    @CreatedDate
    private LocalDateTime registerDate;

    @Column(length = 24)
    private String registerIp;

    public ReviewPic(final Review review, final String picUrl) {
        this.picUrl = picUrl;
        this.review = review;
        review.getReviewPics().add(this);
    }

}
