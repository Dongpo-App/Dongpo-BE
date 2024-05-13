package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.dto.store.AddStoreRequest;
import com.dongyang.dongpo.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping()
    public ResponseEntity addStore(@RequestBody AddStoreRequest request,
                                   @RequestHeader("Authorization") String accessToken) throws Exception {
        return storeService.addStore(request, accessToken);
    }
}
