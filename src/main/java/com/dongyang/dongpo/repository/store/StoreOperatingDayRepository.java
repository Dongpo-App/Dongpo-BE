package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StoreOperatingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreOperatingDayRepository extends JpaRepository<StoreOperatingDay, Long> {
    List<StoreOperatingDay> findByStore(Store store);
}
