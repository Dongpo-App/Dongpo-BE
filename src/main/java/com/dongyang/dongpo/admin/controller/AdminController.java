package com.dongyang.dongpo.admin.controller;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.SignUpDto;
import com.dongyang.dongpo.admin.service.AdminMemberService;
import com.dongyang.dongpo.admin.service.AdminService;
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
    private final AdminMemberService adminMemberService;

    @GetMapping("/login")
    public String login(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:/admin/dashboard";
        }else
            return "admin/login";
    }

    @GetMapping("/register")
    public String signup(Model model){
        model.addAttribute("signupForm", new SignUpDto());
        return "admin/register";
    }

    @PostMapping("/register.do")
    public String signup(@ModelAttribute("signupForm") SignUpDto request){
        adminService.signup(request);
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard/member")
    public String memberBoard(Model model){
        model.addAttribute("members", adminMemberService.findAll());
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/member_board";
    }

    @GetMapping("/dashboard/store")
    public String storeBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/store_board";
    }

    @GetMapping("/dashboard/review")
    public String reviewBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/review_board";
    }

    @GetMapping("/dashboard/confirm")
    public String adminBoard(Model model){
        model.addAttribute("admin", getPrincipal());
        return "admin/dashboard/admin_board";
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin admin = (Admin) authentication.getPrincipal();
        return admin;
    }
}
