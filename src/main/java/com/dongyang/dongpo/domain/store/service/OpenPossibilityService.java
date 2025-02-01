package com.dongyang.dongpo.domain.store.service;

import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;

public interface OpenPossibilityService {
    OpenPossibility getOpenPossibility(Store store);
}
