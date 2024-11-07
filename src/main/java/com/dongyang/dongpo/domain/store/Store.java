package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.bookmark.StoreBookmarkResponseDto;
import com.dongyang.dongpo.dto.store.*;
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
    private List<StoreReviewPic> reviewPics = new ArrayList<>();

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreBookmark> bookmarks = new ArrayList<>();

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

    // 어드민 페이지 점포 조회 응답 용
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

    // 마이페이지 내가 등록한 점포 조회 응답 용
    public StoreSummaryResponseDto toSummaryResponse() {
        return StoreSummaryResponseDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .registerDate(registerDate)
                .build();
    }

    // 점포 간략 정보 응답
    public StoreSummaryResponseDto toSummaryResponse(OpenPossibility openPossibility, List<String> reviewPics) {
        return StoreSummaryResponseDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .status(status)
                .openPossibility(openPossibility)
                .reviewPics(reviewPics)
                .build();
    }

    // 지도 내 주변 점포 조회 응답
    public StoreSummaryResponseDto toSummaryResponse(OpenPossibility openPossibility, Boolean isBookmarked) {
        return StoreSummaryResponseDto.builder()
                .id(id)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .status(status)
                .openPossibility(openPossibility)
                .isBookmarked(isBookmarked)
                .build();
    }

    public StoreDetailsResponseDto toDetailsResponse(Long storeBookmarkCount) {
        return StoreDetailsResponseDto.builder()
                .id(id)
                .openTime(openTime)
                .closeTime(closeTime)
                .isToiletValid(isToiletValid)
                .payMethods(storePayMethods.stream().map(StorePayMethod::getPayMethod).toList())
                .operatingDays(storeOperatingDays.stream().map(StoreOperatingDay::getOperatingDay).toList())
                .visitSuccessfulCount(storeVisitCerts.stream().filter(StoreVisitCert::getIsVisitSuccessful).count())
                .visitFailCount(storeVisitCerts.stream().filter(cert -> !cert.getIsVisitSuccessful()).count())
                .bookmarkCount(storeBookmarkCount)
                .build();
    }

    public StoreBookmarkResponseDto toBookmarkResponse(Long bookmarkCount, Boolean isBookmarked) {
        return StoreBookmarkResponseDto.builder()
                .bookmarkCount((long) bookmarks.size())
                .isMemberBookmarked(isBookmarked)
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
