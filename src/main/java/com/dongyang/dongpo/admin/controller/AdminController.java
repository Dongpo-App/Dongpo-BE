package com.dongyang.dongpo.admin.controller;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.SignUpDto;
import com.dongyang.dongpo.admin.service.AdminMemberService;
import com.dongyang.dongpo.admin.service.AdminReviewService;
import com.dongyang.dongpo.admin.service.AdminService;
import com.dongyang.dongpo.admin.service.AdminStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String login(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken))

            return "redirect:/admin/dashboard";
        else
            return "admin/login";
    }

    @GetMapping("/register")
    public String signup(Model model){
        model.addAttribute("signup", new SignUpDto());
        return "admin/register";
    }

    @PostMapping("/register.do")
    public String signup(@ModelAttribute SignUpDto request){
        adminService.signup(request);
        return "redirect:/admin/login";
    }
}
