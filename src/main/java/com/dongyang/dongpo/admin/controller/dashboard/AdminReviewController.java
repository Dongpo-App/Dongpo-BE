package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.service.store.StoreReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/review")
@RequiredArgsConstructor
public class AdminReviewController {

    private final StoreReviewService storeReviewService;

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id) {
        storeReviewService.findOne(id);
        return "null";
    }
}
