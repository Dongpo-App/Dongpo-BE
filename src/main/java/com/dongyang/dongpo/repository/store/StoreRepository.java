package com.dongyang.dongpo.repository.store;

import com.dongyang.dongpo.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByMemberId(Long memberId);
}
