package com.dongyang.dongpo.domain.store.entity;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.*;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import com.dongyang.dongpo.domain.store.enums.PayMethod;
import com.dongyang.dongpo.domain.store.enums.StoreStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StorePayMethod> storePayMethods = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoreOperatingDay> storeOperatingDays = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreVisitCert> storeVisitCerts = new ArrayList<>();


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
                        .toList())
                .payMethods(storePayMethods.stream()
                        .map(StorePayMethod::getPayMethod)
                        .toList())
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

    // StorePayMethod 등록
    public void addPayMethods(List<PayMethod> payMethods) {
        payMethods.forEach(payMethod -> storePayMethods.add(StorePayMethod.builder()
                .store(this)
                .payMethod(payMethod)
                .build()));
    }

    // StoreOperatingDay 등록
    public void addOperatingDays(List<OperatingDay> operatingDays) {
        operatingDays.forEach(operatingDay -> storeOperatingDays.add(StoreOperatingDay.builder()
                .store(this)
                .operatingDay(operatingDay)
                .build()));
    }

    // 점포 정보 업데이트
    public void updateInfo(StoreInfoUpdateDto updateDto) {
        this.name = updateDto.getName();
        this.openTime = updateDto.getOpenTime();
        this.closeTime = updateDto.getCloseTime();
        this.isToiletValid = updateDto.getIsToiletValid();
        this.status = updateDto.getStatus();
        updatePayMethods(updateDto.getPayMethods());
        updateOperatingDays(updateDto.getOperatingDays());
    }

    private void updatePayMethods(List<PayMethod> newPayMethods) {
        storePayMethods.removeIf(existingPayMethod -> !newPayMethods.contains(existingPayMethod.getPayMethod()));
        newPayMethods.forEach(newPayMethod -> {
            if (storePayMethods.stream().noneMatch(existingPayMethod -> existingPayMethod.getPayMethod().equals(newPayMethod))) {
                storePayMethods.add(StorePayMethod.builder()
                        .store(this)
                        .payMethod(newPayMethod)
                        .build());
            }
        });
    }

    private void updateOperatingDays(List<OperatingDay> newOperatingDays) {
        storeOperatingDays.removeIf(existingOperatingDay -> !newOperatingDays.contains(existingOperatingDay.getOperatingDay()));
        newOperatingDays.forEach(newOperatingDay -> {
            if (storeOperatingDays.stream().noneMatch(existingOperatingDay -> existingOperatingDay.getOperatingDay().equals(newOperatingDay))) {
                storeOperatingDays.add(StoreOperatingDay.builder()
                        .store(this)
                        .operatingDay(newOperatingDay)
                        .build());
            }
        });
    }

    public void addReportCount() {
        this.reportCount++;
        if (this.reportCount >= 5) {
            updateStoreStatusHidden();
            this.reportCount = 0;
        }
    }

    public void updateStoreStatusDeleted() {
        this.status = StoreStatus.DELETED;
    }

    private void updateStoreStatusHidden() {
        this.status = StoreStatus.HIDDEN;
    }
}
