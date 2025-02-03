package com.dongyang.dongpo.domain.review.entity;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.review.dto.MyRegisteredReviewsResponseDto;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.review.dto.ReviewDto;
import com.dongyang.dongpo.domain.review.dto.ReviewRegisteredBy;
import com.dongyang.dongpo.domain.review.dto.ReviewResponseDto;
import com.dongyang.dongpo.domain.review.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_review")
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Double reviewStar;

    @Column(columnDefinition = "TEXT")
    private String text;

    @CreatedDate
    private LocalDateTime registerDate;

    @Column(length = 24)
    private String registerIp;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.VISIBLE;

    @Builder.Default
    private Integer reportCount = 0;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewPic> reviewPics = new ArrayList<>();

    public void delete() {
        this.status = ReviewStatus.DELETED;
    }

    public void addReviewPics(List<String> reviewPics) {
        reviewPics.forEach(picUrl -> {
            this.reviewPics.add(new ReviewPic(this, picUrl));
        });
    }

    public ReviewDto toResponse() {
        List<String> reviewOnlyPic = reviewPics.stream().map(ReviewPic::getPicUrl).toList();

        return ReviewDto.builder()
                .id(this.id)
                .registerDate(this.registerDate)
                .reviewStar(this.reviewStar)
                .reviewText(this.text)
                .memberId(this.member.getId())
                .memberNickname(this.member.getNickname())
                .memberMainTitle(this.member.getMainTitle().getDescription())
                .memberProfilePic(this.member.getProfilePic())
                .storeId(this.store.getId())
                .storeName(this.store.getName())
                .reviewPics(reviewOnlyPic)
                .build();
    }

    public ReviewResponseDto toStoreReviewResponse() {
        return ReviewResponseDto.builder()
                .id(this.id)
                .registeredBy(ReviewRegisteredBy.builder()
                        .memberId(this.member.getId())
                        .memberNickname(this.member.getNickname())
                        .memberMainTitle(this.member.getMainTitle().getDescription())
                        .memberProfilePic(this.member.getProfilePic())
                        .build())
                .reviewStar(this.reviewStar)
                .reviewText(this.text)
                .reviewPics(this.reviewPics.stream()
                        .map(ReviewPic::getPicUrl)
                        .toList())
                .registerDate(this.registerDate)
                .build();
    }

    public MyRegisteredReviewsResponseDto toMyRegisteredReviewsResponse() {
        return MyRegisteredReviewsResponseDto.builder()
                .id(this.id)
                .storeName(this.store.getName())
                .reviewStar(this.reviewStar)
                .reviewText(this.text)
                .reviewPics(this.reviewPics.stream()
                        .map(ReviewPic::getPicUrl)
                        .toList())
                .registerDate(this.registerDate)
                .build();
    }

    public void addReportCount() {
        this.reportCount++;
        if (this.reportCount >= 5) {
            updateReviewStatusHidden();
            this.reportCount = 0;
        }
    }

    private void updateReviewStatusHidden() {
        this.status = ReviewStatus.HIDDEN;
    }
}
