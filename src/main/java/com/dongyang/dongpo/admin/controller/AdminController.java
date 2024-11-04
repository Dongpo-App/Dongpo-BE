package com.dongyang.dongpo.admin.controller;


import com.dongyang.dongpo.dto.admin.SignUpDto;
import com.dongyang.dongpo.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

            return "redirect:/admin/dashboard/member";
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
