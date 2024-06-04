package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.domain.report.ReviewReport;
import com.dongyang.dongpo.repository.report.ReviewReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewReportService {

    private final ReviewReportRepository reviewReportRepository;

    public List<ReviewReport> findAll(){
        return reviewReportRepository.findAll();
    }
}
