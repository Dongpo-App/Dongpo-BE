package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StoreOperatingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreOperatingDayRepository extends JpaRepository<StoreOperatingDay, Long> {
    List<StoreOperatingDay> findByStore(Store store);
}
