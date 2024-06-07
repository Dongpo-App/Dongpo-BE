package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.dto.ConfrimDto;
import com.dongyang.dongpo.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/grant")
@RequiredArgsConstructor
public class AdminConfirmController {

    private final AdminMemberService adminMemberService;

    @PostMapping("/process")
    public String approve(@ModelAttribute ConfrimDto confrimDto,
                          RedirectAttributes redirectAttributes){

        if (confrimDto.getAction().equals("approve")) {
            adminMemberService.approveAdmin(confrimDto);
            redirectAttributes.addFlashAttribute("message", "Selected grants approved successfully.");
        }else {
            adminMemberService.rejectAdmin(confrimDto);
            redirectAttributes.addFlashAttribute("message", "Selected grants rejected successfully.");
        }

        return "redirect:/admin/dashboard/confirm";
    }
}
