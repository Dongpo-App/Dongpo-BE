package com.dongyang.dongpo.admin.controller.dashboard;

import com.dongyang.dongpo.exception.store.StoreNotFoundException;
import com.dongyang.dongpo.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final StoreService storeService;

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id) throws StoreNotFoundException {
        storeService.findOne(id);
        return "null";
    }
}
