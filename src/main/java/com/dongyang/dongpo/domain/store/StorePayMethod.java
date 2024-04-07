package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.domain.PK.StorePayMethodId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_pay_method")
@IdClass(StorePayMethodId.class)
public class StorePayMethod {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    @MapsId
    private Store storeId;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    public enum PayMethod {
        CASH, CARD, TRANSFER
    }
}
