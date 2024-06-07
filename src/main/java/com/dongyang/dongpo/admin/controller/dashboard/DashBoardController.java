package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.ConfrimDto;
import com.dongyang.dongpo.admin.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final AdminMemberService adminMemberService;
    private final AdminReviewService adminReviewService;
    private final AdminStoreService adminStoreService;
    private final AdminStoreReportService adminStoreReportService;
    private final AdminReviewReportService adminReviewReportService;
    private final AdminNoticeService adminNoticeService;


    @GetMapping("/member")
    public String memberBoard(Model model){
        model.addAttribute("members", adminMemberService.findAll());
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/member/member_board";
    }

    @GetMapping("/store")
    public String storeBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("stores", adminStoreService.findAll());
        return "admin/dashboard/store/store_board";
    }

    @GetMapping("/review")
    public String reviewBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reviews", adminReviewService.findAll());
        return "admin/dashboard/review/review_board";
    }

    @GetMapping("/confirm")
    public String adminBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("grants", adminMemberService.findProcessAdmin());
        model.addAttribute("confirm", new ConfrimDto());
        return "admin/dashboard/admin_board";
    }

    @GetMapping("/store/report")
    public String storeReport(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reports", adminStoreReportService.findAll());

        return "admin/dashboard/store/store_report_board";
    }

    @GetMapping("/review/report")
    public String reviewReport(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reports", adminReviewReportService.findAll());

        return "admin/dashboard/review/review_report_board";
    }

    @GetMapping("/notice")
    public String notice(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("notices", adminNoticeService.findAll());

        return "admin/dashboard/notice/notice_board";
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Admin) authentication.getPrincipal();
    }
}
