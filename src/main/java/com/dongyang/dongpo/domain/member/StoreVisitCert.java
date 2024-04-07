package com.dongyang.dongpo.domain.member;

import com.dongyang.dongpo.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "store_visit_cert")
public class StoreVisitCert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_by")
    private Member certBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cert_store")
    private Store certStore;

    private boolean isVisitSuccessful;

    @Column(length = 24)
    private String certIp;

    private int certTimeRange;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime certDate;
}
