package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_visit_cert")
public class StoreVisitCert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_store")
    private Store store;

    private Boolean isVisitSuccessful;

    @Column(length = 24)
    private String certIp;

    private int certTimeRange;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime certDate;

    @Enumerated(EnumType.STRING)
    private DayOfWeek openDay;

    @Enumerated(EnumType.STRING)
    private OpenTime openTime;
}
