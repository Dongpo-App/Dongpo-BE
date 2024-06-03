package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/grant")
@RequiredArgsConstructor
public class AdminConfirmController {

    private final AdminMemberService adminMemberService;

    @PostMapping("/approve")
    public String approve(@RequestParam("id") Long id){
        adminMemberService.approveAdmin(id);
        return "redirect:/admin/dashboard/confirm";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam("id") Long id){
        adminMemberService.rejectAdmin(id);
        return "redirect:/admin/dashboard/confirm";
    }
}
