package com.dongyang.dongpo.admin.controller.notice;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.dto.notice.NoticeDto;
import com.dongyang.dongpo.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @GetMapping("/write")
    public String write(Model model, @AuthenticationPrincipal Admin admin) {
        model.addAttribute("admin", admin);
        model.addAttribute("notice", new NoticeDto());
        return "admin/dashboard/notice/notice_write";
    }

    @PostMapping("/write.do")
    public String write(@ModelAttribute NoticeDto noticeDto,
                        @RequestParam("images") List<MultipartFile> images,
                        @AuthenticationPrincipal Admin admin) throws IOException {
        noticeService.addNotice(noticeDto, admin, images);
        return "redirect:/admin/dashboard/notice";
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public ResponseEntity<NoticeDto> detail(@PathVariable Long id){
        return ResponseEntity.ok(noticeService.detail(id));
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        noticeService.delete(id);
        return "redirect:/admin/dashboard/notice";
    }
}
