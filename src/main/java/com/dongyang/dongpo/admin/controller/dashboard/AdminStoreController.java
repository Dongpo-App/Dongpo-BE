package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.service.report.ReportService;
import com.dongyang.dongpo.service.store.StoreService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final StoreService storeService;
    private final ReportService reportService;

    @GetMapping("/{id}")
    public String storeDetail(@PathVariable Long id, Model model,
        @AuthenticationPrincipal Admin admin) {
        model.addAttribute("store", storeService.findOne(id));
        model.addAttribute("admin", admin);
        return "admin/dashboard/store/store_detail";
    }

    @GetMapping("/report/{id}")
    public String reportDetail(@PathVariable Long id, Model model,
        @AuthenticationPrincipal Admin admin) {
        model.addAttribute("report", reportService.findOneStoreReport(id));
        model.addAttribute("admin", admin);
        return "admin/dashboard/store/store_report_detail";
    }
}
