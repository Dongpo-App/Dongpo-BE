package com.dongyang.dongpo.domain.PK;

import com.dongyang.dongpo.domain.store.OperatingDay;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class StoreOperatingDayId implements Serializable {
    private Long id;
    private OperatingDay operatingDay;
}
