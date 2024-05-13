package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.store.ReviewDto;
import com.dongyang.dongpo.dto.store.StoreDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    private boolean isToiletValid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(length = 24)
    private String registerIp;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StoreStatus status = StoreStatus.ACTIVE;

    @Builder.Default
    private Integer reportCount = 0;

    @ElementCollection(targetClass = PayMethod.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "store_pay_method", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "payMethod")
    private List<PayMethod> payMethods;

    @ElementCollection(targetClass = OperatingDay.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "store_operating_day", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "operatingDay")
    private List<OperatingDay> operatingDays;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreReview> reviews = new ArrayList<>();

    public enum StoreStatus {
        ACTIVE, INACTIVE, HIDDEN, CLOSED
    }

    public StoreDto toResponse(){
        return StoreDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .memberId(member.getId())
                .openTime(openTime)
                .closeTime(closeTime)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDays)
                .payMethods(payMethods)
                .status(status)
                .build();
    }

    public StoreDto toResponse(List<ReviewDto> reviewDtos){
        return StoreDto.builder()
                .id(id)
                .name(name)
                .address(address)
                .memberId(member.getId())
                .openTime(openTime)
                .closeTime(closeTime)
                .isToiletValid(isToiletValid)
                .operatingDays(operatingDays)
                .payMethods(payMethods)
                .status(status)
                .reviews(reviewDtos)
                .build();
    }


    public void update(StoreDto storeDto){
        this.name = storeDto.getName();
        this.address = storeDto.getAddress();
        this.openTime = storeDto.getOpenTime();
        this.closeTime = storeDto.getCloseTime();
        this.isToiletValid = storeDto.isToiletValid();
        this.payMethods = storeDto.getPayMethods();
        this.operatingDays = storeDto.getOperatingDays();
    }
}
