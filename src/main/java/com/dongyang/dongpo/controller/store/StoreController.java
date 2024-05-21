package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("")
    @Operation(summary = "전체 점포 조회")
    public ResponseEntity allStore(){
        return storeService.allStore();
    }

    @GetMapping("/{id}")
    @Operation(summary = "점포 상세 조회")
    public ResponseEntity detailStore(@PathVariable Long id) throws Exception {
        return storeService.detailStore(id);
    }

    @GetMapping("/member")
    @Operation(summary = "내가 등록한 점포조회")
    public ResponseEntity myRegStore(@RequestHeader("Authorization") String accessToken) throws Exception {
        return storeService.myRegStore(accessToken);
    }

    @PostMapping("")
    @Operation(summary = "점포 등록")
    public ResponseEntity addStore(@RequestBody StoreDto request,
                                   @RequestHeader("Authorization") String accessToken) throws Exception {
        return storeService.addStore(request, accessToken);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "점포 삭제")
    public ResponseEntity deleteStore(@PathVariable Long id){
        return storeService.deleteStore(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "점포 정보 수정")
    public ResponseEntity updateStore(@PathVariable Long id,
                                      @RequestBody StoreDto request) throws Exception {
        return storeService.updateStore(id, request);
    }
}
