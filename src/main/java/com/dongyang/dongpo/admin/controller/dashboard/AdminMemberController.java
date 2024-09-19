package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id) {
        memberService.findOne(id);
        return "null";
    }



}
