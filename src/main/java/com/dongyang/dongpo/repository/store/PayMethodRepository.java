package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.StorePayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMethodRepository extends JpaRepository<StorePayMethod, Long> {
}
