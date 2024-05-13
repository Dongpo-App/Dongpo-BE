package com.dongyang.dongpo.domain.store;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_pay_method")
//@IdClass(StorePayMethodId.class)
public class StorePayMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    public enum PayMethod {
        CASH, CARD, TRANSFER
    }
}
