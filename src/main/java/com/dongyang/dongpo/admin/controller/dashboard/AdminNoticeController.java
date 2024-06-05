package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.NoticeDto;
import com.dongyang.dongpo.admin.service.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("notice", new NoticeDto());
        return "admin/dashboard/notice/notice_write";
    }

    @PostMapping("/write.do")
    public String write(@ModelAttribute NoticeDto noticeDto){
        adminNoticeService.addNotice(noticeDto, getPrincipal());
        return "redirect:/admin/dashboard/notice";
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Admin) authentication.getPrincipal();
    }
}
