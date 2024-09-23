package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.domain.store.StorePayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorePayMethodRepository extends JpaRepository<StorePayMethod, Long> {
    List<StorePayMethod> findByStore(Store store);
}
