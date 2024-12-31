package com.dongyang.dongpo.domain.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.dto.ConfrimDto;
import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.admin.service.AdminService;
import com.dongyang.dongpo.domain.admin.service.NoticeService;
import com.dongyang.dongpo.domain.member.service.MemberService;
import com.dongyang.dongpo.domain.report.service.ReportService;
import com.dongyang.dongpo.domain.store.service.StoreReviewService;
import com.dongyang.dongpo.domain.store.service.StoreService;
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

    private final MemberService memberService;
    private final AdminService adminService;
    private final NoticeService noticeService;
    private final StoreService storeService;
    private final StoreReviewService reviewService;
    private final ReportService reportService;


    @GetMapping("/member")
    public String memberBoard(Model model){
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/member/member_board";
    }

    @GetMapping("/store")
    public String storeBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("stores", storeService.findAll());
        return "admin/dashboard/store/store_board";
    }

    @GetMapping("/review")
    public String reviewBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/dashboard/review/review_board";
    }

    @GetMapping("/confirm")
    public String adminBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("grants", adminService.findProcessAdmin());
        model.addAttribute("confirm", new ConfrimDto());
        return "admin/dashboard/admin_board";
    }

    @GetMapping("/store/report")
    public String storeReport(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reports", reportService.findAllStoreReport());

        return "admin/dashboard/store/store_report_board";
    }

    @GetMapping("/review/report")
    public String reviewReport(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("reports", reportService.findAllReviewReport());

        return "admin/dashboard/review/review_report_board";
    }

    @GetMapping("/notice")
    public String notice(Model model){
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("notices", noticeService.findAll());

        return "admin/dashboard/notice/notice_board";
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Admin) authentication.getPrincipal();
    }
}
