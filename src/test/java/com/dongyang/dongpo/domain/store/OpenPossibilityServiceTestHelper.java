package com.dongyang.dongpo.domain.store;

import com.dongyang.dongpo.common.util.store.StoreUtil;
import com.dongyang.dongpo.domain.store.entity.Store;
import com.dongyang.dongpo.domain.store.enums.OpenPossibility;
import com.dongyang.dongpo.domain.store.service.OpenPossibilityService;
import com.dongyang.dongpo.domain.store.service.StoreVisitCertService;

public class OpenPossibilityServiceTestHelper extends OpenPossibilityService {
    public OpenPossibilityServiceTestHelper(StoreVisitCertService storeVisitCertService, StoreUtil storeUtil) {
        super(storeVisitCertService, storeUtil);
    }

    @Override
    protected OpenPossibility getOpenPossibility(Store store) {
        return super.getOpenPossibility(store);
    }
}
