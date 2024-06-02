package com.dongyang.dongpo.admin.controller;

import com.dongyang.dongpo.admin.dto.SignUpDto;
import com.dongyang.dongpo.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
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
        return "admin/login";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("signupForm", new SignUpDto());
        return "admin/signup";
    }

    @PostMapping("/signup.do")
    public String signup(@ModelAttribute("signupForm") SignUpDto request){
        adminService.signup(request);
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "admin/dashboard";
    }
}
