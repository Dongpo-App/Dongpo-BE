package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
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

    private boolean isToiletValid = false;

    @ManyToOne
    @JoinColumn(name = "registered_by")
    private Member registeredMemberId;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registerDate;

    @Column(length = 24)
    private String registerIp;

    @Enumerated(EnumType.STRING)
    private StoreStatus status = StoreStatus.ACTIVE;

    private Integer reportCount = 0;

    public enum StoreStatus {
        ACTIVE, INACTIVE, HIDDEN, CLOSED
    }
}
