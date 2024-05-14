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

    @GetMapping("")
    public ResponseEntity allStore(){

        return storeService.allStore();
    }

    @GetMapping("/{id}")
    public ResponseEntity detailStore(@PathVariable Long id,
                                      @RequestHeader("Authorization") String accessToken){

        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity addStore(@RequestBody AddStoreRequest request,
                                   @RequestHeader("Authorization") String accessToken) throws Exception {
        return storeService.addStore(request, accessToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStore(@PathVariable Long id,
                                      @RequestHeader("Authorization") String accessToken){
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateStore(@PathVariable Long id,
                                      @RequestHeader("Authorization") String accessToken){
        return ResponseEntity.ok().build();
    }
}
