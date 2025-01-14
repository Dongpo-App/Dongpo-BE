package com.dongyang.dongpo.domain.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.entity.Admin;
import com.dongyang.dongpo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model,
        @AuthenticationPrincipal Admin admin) {
        model.addAttribute("member", memberService.findById(id));
        model.addAttribute("admin", admin);
        return "admin/dashboard/member/member_detail";
    }

}
