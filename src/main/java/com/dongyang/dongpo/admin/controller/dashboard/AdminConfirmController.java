package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.admin.service.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/grant")
@RequiredArgsConstructor
public class AdminConfirmController {

    private final AdminMemberService adminMemberService;

    @PostMapping("/process")
    public String approve(@RequestParam("selectedGrants") List<Long> selectedGrants,
                          @RequestParam("action") String action,
                          RedirectAttributes redirectAttributes){

        if (action.equals("approve")) {
            adminMemberService.approveAdmin(selectedGrants);
            redirectAttributes.addFlashAttribute("message", "Selected grants approved successfully.");
        }else {
            adminMemberService.rejectAdmin(selectedGrants);
            redirectAttributes.addFlashAttribute("message", "Selected grants rejected successfully.");
        }

        return "redirect:/admin/dashboard/confirm";
    }

}
