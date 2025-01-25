package com.dongyang.dongpo.domain.store.repository;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ReadOnlyStoreRepository extends StoreRepository {
}
