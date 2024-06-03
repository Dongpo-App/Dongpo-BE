package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.service.AdminMemberService;
import com.dongyang.dongpo.admin.service.AdminReviewService;
import com.dongyang.dongpo.admin.service.AdminStoreService;
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
        return "admin/dashboard/admin_board";
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Admin) authentication.getPrincipal();
    }
}
