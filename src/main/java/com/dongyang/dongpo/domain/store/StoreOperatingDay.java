package com.dongyang.dongpo.domain.store;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_operating_day")
//@IdClass(StoreOperatingDayId.class)
public class StoreOperatingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;


    @Enumerated(EnumType.STRING)
    private OperatingDay operatingDay;

    public enum OperatingDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

}