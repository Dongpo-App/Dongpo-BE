package com.dongyang.dongpo.domain.PK;

import com.dongyang.dongpo.domain.store.StorePayMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class StorePayMethodId implements Serializable {
    private Long id;
    private StorePayMethod.PayMethod payMethod;
}
