package com.dongyang.dongpo.domain.store.entity;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.ReviewDto;
import com.dongyang.dongpo.domain.store.dto.ReviewRegisteredBy;
import com.dongyang.dongpo.domain.store.dto.StoreReviewResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Double reviewStar;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(length = 24)
    private String registerIp;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.VISIBLE;

    @Builder.Default
    private Integer reportCount = 0;

    @OneToMany(mappedBy = "reviewId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreReviewPic> reviewPics = new ArrayList<>();

    public void delete() {
        this.status = ReviewStatus.DELETED;
    }

    public enum ReviewStatus {
        VISIBLE, HIDDEN, DELETED
    }

    public void addReviewPic(StoreReviewPic reviewPic) {
        reviewPics.add(reviewPic);
        reviewPic.addReview(this);
    }

    public ReviewDto toResponse(){
        List<String> reviewOnlyPic = reviewPics.stream().map(StoreReviewPic::getPicUrl).toList();

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

    public StoreReviewResponseDto toStoreReviewResponse() {
        return StoreReviewResponseDto.builder()
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
                        .map(StoreReviewPic::getPicUrl)
                        .toList())
                .registerDate(this.registerDate)
                .build();
    }

    public ReviewDto toMyPageResponse(){
        List<String> reviewOnlyPic = reviewPics.stream().map(StoreReviewPic::getPicUrl).toList();

        return ReviewDto.builder()
                .id(this.id)
                .registerDate(this.registerDate)
                .reviewStar(this.reviewStar)
                .reviewText(this.text)
                .storeId(this.store.getId())
                .storeName(this.store.getName())
                .reviewPics(reviewOnlyPic)
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
