package com.dongyang.dongpo.domain.store.repository;

import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.entity.StorePayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorePayMethodRepository extends JpaRepository<StorePayMethod, Long> {
    List<StorePayMethod> findByStore(Store store);
}
