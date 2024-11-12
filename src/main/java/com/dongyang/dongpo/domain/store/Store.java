package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.store.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_table")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128)
    private String name;

    private double latitude;

    private double longitude;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    private Boolean isToiletValid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(length = 24)
    private String registerIp;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StoreStatus status = StoreStatus.ACTIVE;

    @Builder.Default
    private Integer reportCount = 0;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StorePayMethod> storePayMethods = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreOperatingDay> storeOperatingDays = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreVisitCert> storeVisitCerts = new ArrayList<>();

    public enum StoreStatus {
        ACTIVE, INACTIVE, HIDDEN, CLOSED
    }

    public enum OperatingDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

    public enum PayMethod {
        CASH, CARD, TRANSFER
    }



    public StoreDto toResponse() {
        List<Store.OperatingDay> operatingDayValues = this.storeOperatingDays.stream()
                .map(StoreOperatingDay::getOperatingDay)
                .collect(Collectors.toList());

        List<Store.PayMethod> payMethodValues = this.storePayMethods.stream()
                .map(StorePayMethod::getPayMethod)
                .collect(Collectors.toList());

        List<ReviewDto> reviewDtos = this.reviews.stream()
                .filter(review -> review.getStatus().equals(StoreReview.ReviewStatus.VISIBLE))
                .map(StoreReview::toResponse)
                .toList();

        return StoreDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .reportCount(reportCount)
                .memberId(member.getId())
                .openTime(openTime)
                .closeTime(closeTime)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDayValues)
                .payMethods(payMethodValues)
                .status(status)
                .reviews(reviewDtos)
                .build();
    }

    public StoreSummaryDto toSummaryResponse() {
        return StoreSummaryDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .registerDate(registerDate)
                .build();
    }

    public StoreSummaryDto toSummaryResponse(OpenPossibility openPossibility, Boolean isBookmarked, List<String> reviewPics) {
        return StoreSummaryDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .status(status)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .reviewPics(reviewPics)
                .build();
    }

    public StoreSummaryDto toSummaryResponse(Boolean isBookmarked, OpenPossibility openPossibility) {
        return StoreSummaryDto.builder()
                .id(id)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .status(status)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .build();
    }
          
    public StoreDto toResponse(OpenPossibility openPossibility, boolean isBookmarked, Long bookmarkCount) {
        List<Store.OperatingDay> operatingDayValues = this.storeOperatingDays.stream()
                .map(StoreOperatingDay::getOperatingDay)
                .collect(Collectors.toList());

        List<Store.PayMethod> payMethodValues = this.storePayMethods.stream()
                .map(StorePayMethod::getPayMethod)
                .collect(Collectors.toList());

        List<ReviewDto> reviewDtos = this.reviews.stream()
                .sorted(Comparator.comparingLong(StoreReview::getId).reversed())
                .map(StoreReview::toResponse)
                .limit(3)
                .toList();

        Long visitSuccessfulCount = storeVisitCerts.stream()
                .filter(StoreVisitCert::getIsVisitSuccessful)
                .count();

        Long visitFailCount = storeVisitCerts.stream()
                .filter(cert -> !cert.getIsVisitSuccessful())
                .count();

        return StoreDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .reportCount(reportCount)
                .memberId(member.getId())
                .memberNickname(member.getNickname())
                .openTime(openTime)
                .closeTime(closeTime)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDayValues)
                .payMethods(payMethodValues)
                .status(status)
                .reviews(reviewDtos)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .visitSuccessfulCount(visitSuccessfulCount)
                .visitFailCount(visitFailCount)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    public void update(StoreUpdateDto updateDto){
        this.name = updateDto.getName();
        this.openTime = updateDto.getOpenTime();
        this.closeTime = updateDto.getCloseTime();
        this.isToiletValid = updateDto.getIsToiletValid();
        this.status = updateDto.getStatus();
    }

    public Store addReport(){
        this.reportCount++;
        return this;
    }
}
