package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.dto.admin.ConfrimDto;
import com.dongyang.dongpo.service.admin.AdminService;
import com.dongyang.dongpo.service.notice.NoticeService;
import com.dongyang.dongpo.service.member.MemberService;
import com.dongyang.dongpo.service.report.ReportService;
import com.dongyang.dongpo.service.store.StoreReviewService;
import com.dongyang.dongpo.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String memberBoard(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("admin", admin);
        return "admin/dashboard/member/member_board";
    }

    @GetMapping("/store")
    public String storeBoard(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("stores", storeService.findAll());
        return "admin/dashboard/store/store_board";
    }

    @GetMapping("/review")
    public String reviewBoard(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("reviews", reviewService.findAll());
        return "admin/dashboard/review/review_board";
    }

    @GetMapping("/confirm")
    public String adminBoard(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("grants", adminService.findProcessAdmin());
        model.addAttribute("confirm", new ConfrimDto());
        return "admin/dashboard/admin_board";
    }

    @GetMapping("/store/report")
    public String storeReport(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("reports", reportService.findAllStoreReport());

        return "admin/dashboard/store/store_report_board";
    }

    @GetMapping("/review/report")
    public String reviewReport(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("reports", reportService.findAllReviewReport());

        return "admin/dashboard/review/review_report_board";
    }

    @GetMapping("/notice")
    public String notice(Model model, @AuthenticationPrincipal Admin admin){
        model.addAttribute("admin", admin);
        model.addAttribute("notices", noticeService.findAll());

        return "admin/dashboard/notice/notice_board";
    }
}
