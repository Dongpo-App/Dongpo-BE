package com.dongyang.dongpo.domain.store.entity;

import com.dongyang.dongpo.domain.store.enums.OperatingDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_id", "operating_day"})
})
public class StoreOperatingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private OperatingDay operatingDay;

    public StoreOperatingDay(Store store, OperatingDay operatingDay) {
        this.operatingDay = operatingDay;
        this.store = store;
        store.getStoreOperatingDays().add(this);
    }
}
