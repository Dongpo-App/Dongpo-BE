package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.dto.notice.NoticeDto;
import com.dongyang.dongpo.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("admin", getPrincipal());
        model.addAttribute("notice", new NoticeDto());
        return "admin/dashboard/notice/notice_write";
    }

    @PostMapping("/write.do")
    public String write(@ModelAttribute NoticeDto noticeDto){
        noticeService.addNotice(noticeDto, getPrincipal());
        return "redirect:/admin/dashboard/notice";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public ResponseEntity<NoticeDto> detail(@PathVariable Long id){
        return ResponseEntity.ok(noticeService.detail(id));
    }

    private Admin getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Admin) authentication.getPrincipal();
    }
}
