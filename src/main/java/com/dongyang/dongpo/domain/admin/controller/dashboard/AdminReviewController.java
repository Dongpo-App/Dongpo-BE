package com.dongyang.dongpo.domain.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.report.service.ReportService;
import com.dongyang.dongpo.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/review")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;
    private final ReportService reportService;

    @GetMapping("/{id}")
    public String reviewDetail(@PathVariable("id") Long id, Model model,
        @AuthenticationPrincipal Admin admin) {
        model.addAttribute("review", reviewService.findOne(id));
        model.addAttribute("admin", admin);
        return "admin/dashboard/review/review_detail";
    }

    @GetMapping("/report/{id}")
    public String reportDetail(@PathVariable Long id, Model model,
        @AuthenticationPrincipal Admin admin) {
        model.addAttribute("report", reportService.findOneReviewReport(id));
        model.addAttribute("admin", admin);
        return "admin/dashboard/review/review_report_detail";
    }
}
