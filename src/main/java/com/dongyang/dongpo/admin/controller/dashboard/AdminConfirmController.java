package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.dto.admin.ConfrimDto;
import com.dongyang.dongpo.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/grant")
@RequiredArgsConstructor
public class AdminConfirmController {

    private final AdminService adminService;

    @PostMapping("/process")
    public String approve(@ModelAttribute ConfrimDto confrimDto,
                          RedirectAttributes redirectAttributes){

        if (confrimDto.getAction().equals("approve")) {
            adminService.approveAdmin(confrimDto);
            redirectAttributes.addFlashAttribute("message", "Selected grants approved successfully.");
        }else {
            adminService.rejectAdmin(confrimDto);
            redirectAttributes.addFlashAttribute("message", "Selected grants rejected successfully.");
        }

        return "redirect:/admin/dashboard/confirm";
    }
}
