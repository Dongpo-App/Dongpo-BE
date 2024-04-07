package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.PK.StoreOperatingDayId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_operating_day")
@IdClass(StoreOperatingDayId.class)
public class StoreOperatingDay {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    @MapsId
    private Store storeId;

    @Id
    @Enumerated(EnumType.STRING)
    private OperatingDay operatingDay;

    public enum OperatingDay {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}