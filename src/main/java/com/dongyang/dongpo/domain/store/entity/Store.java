package com.dongyang.dongpo.domain.store.entity;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
                .operatingDays(storeOperatingDays.stream()
                        .map(StoreOperatingDay::getOperatingDay)
                        .collect(Collectors.toList()))
                .payMethods(storePayMethods.stream()
                        .map(StorePayMethod::getPayMethod)
                        .collect(Collectors.toList()))
                .status(status)
                .build();
    }

    public StoreDetailInfoResponseDto toDetailInfoResponse(final OpenPossibility openPossibility,
                                                           final boolean isBookmarked,
                                                           final Long bookmarkCount,
                                                           final List<String> reviewPics) {
        return StoreDetailInfoResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .address(this.address)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .reviewPics(reviewPics)
                .registeredBy(RegisteredBy.builder()
                        .memberId(this.member.getId())
                        .memberNickname(this.member.getNickname())
                        .build())
                .operatingTime(OperatingTime.builder()
                        .openTime(this.openTime)
                        .closeTime(this.closeTime)
                        .build())
                .isToiletValid(this.isToiletValid)
                .operatingDays(this.storeOperatingDays.stream()
                        .map(StoreOperatingDay::getOperatingDay)
                        .toList())
                .payMethods(this.storePayMethods.stream()
                        .map(StorePayMethod::getPayMethod)
                        .toList())
                .visitSuccessCount(this.storeVisitCerts.stream()
                        .filter(StoreVisitCert::getIsVisitSuccessful)
                        .count())
                .visitFailCount(this.storeVisitCerts.stream()
                        .filter(cert -> !cert.getIsVisitSuccessful())
                        .count())
                .bookmarkCount(bookmarkCount)
                .build();
    }

    public NearbyStoresResponseDto toNearbyStoresResponse(final Boolean isBookmarked) {
        return NearbyStoresResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isBookmarked(isBookmarked)
                .build();
    }

    public MyRegisteredStoresResponseDto toMyRegisteredStoresResponse() {
        return MyRegisteredStoresResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .address(this.address)
                .registerDate(this.registerDate)
                .build();
    }

    public StoreBasicInfoResponseDto toBasicInfoResponse(OpenPossibility openPossibility, Boolean isBookmarked, List<String> reviewPics) {
        return StoreBasicInfoResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .address(this.address)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .reviewPics(reviewPics)
                .build();
    }

    public void update(StoreUpdateDto updateDto) {
        this.name = updateDto.getName();
        this.openTime = updateDto.getOpenTime();
        this.closeTime = updateDto.getCloseTime();
        this.isToiletValid = updateDto.getIsToiletValid();
        this.status = updateDto.getStatus();
    }

    public void addReportCount() {
        this.reportCount++;
        if (this.reportCount >= 5) {
            updateStoreStatusHidden();
            this.reportCount = 0;
        }
    }

    private void updateStoreStatusHidden() {
        this.status = StoreStatus.HIDDEN;
    }
}
