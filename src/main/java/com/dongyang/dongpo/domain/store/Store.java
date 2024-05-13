package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(length = 255)
    private String location;

    private LocalTime openTime;

    private LocalTime closeTime;

    private boolean isToiletValid;

    @ManyToOne
    private Member member;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Builder.Default
    private LocalDateTime registerDate = LocalDateTime.now();

    @Column(length = 24)
    private String registerIp;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StoreStatus status = StoreStatus.ACTIVE;

    @Builder.Default
    private Integer reportCount = 0;

    public enum StoreStatus {
        ACTIVE, INACTIVE, HIDDEN, CLOSED
    }
}
