package com.dongyang.dongpo.domain.store.entity;

import com.dongyang.dongpo.domain.store.enums.PayMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_id", "pay_method"})
})
public class StorePayMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(columnDefinition = "VARCHAR(255)")
    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    public StorePayMethod(Store store, PayMethod payMethod) {
        this.payMethod = payMethod;
        this.store = store;
        store.getStorePayMethods().add(this);
    }
}
