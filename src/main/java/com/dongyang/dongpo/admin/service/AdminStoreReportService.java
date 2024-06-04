package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.domain.report.StoreReport;
import com.dongyang.dongpo.repository.report.StoreReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStoreReportService {

    private final StoreReportRepository storeReportRepository;

    public List<StoreReport> findAll(){
        return storeReportRepository.findAll();
    }
}
