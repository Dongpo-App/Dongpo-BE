package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.StoreDto;
import com.dongyang.dongpo.dto.store.StoreRegisterDto;
import com.dongyang.dongpo.dto.store.StoreUpdateDto;
import com.dongyang.dongpo.service.store.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

//    @GetMapping("")
//    @Operation(summary = "전체 점포 조회")
//    public ResponseEntity<ApiResponse<List<StoreDto>>> allStore(){
//        return ResponseEntity.ok(new ApiResponse<>(storeService.findAll()));
//    }

    @GetMapping("")
    @Operation(summary = "현재 위치 기준 주변 점포 조회")
    public ResponseEntity<ApiResponse<List<StoreDto>>> getStoresByCurrentLocation(@ModelAttribute LatLong latLong) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.findStoresByCurrentLocation(latLong)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "점포 상세 조회")
    public ResponseEntity<ApiResponse<StoreDto>> detailStore(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(new ApiResponse<>(storeService.detailStore(id)));
    }

    @GetMapping("/member")
    @Operation(summary = "내가 등록한 점포조회")
    public ResponseEntity<ApiResponse<List<StoreDto>>> myRegStore(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.myRegStore(member)));
    }

    @PostMapping("")
    @Operation(summary = "점포 등록")
    public ResponseEntity<ApiResponse<String>> addStore(@RequestBody StoreRegisterDto request,
                                                        @AuthenticationPrincipal Member member) {
        storeService.addStore(request, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "점포 삭제")
    public ResponseEntity<ApiResponse<String>> deleteStore(@PathVariable Long id){
        storeService.deleteStore(id);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "점포 정보 수정")
    public ResponseEntity<ApiResponse<String>> updateStore(@PathVariable Long id,
                                                           @RequestBody StoreUpdateDto request,
                                                           @AuthenticationPrincipal Member member) {
        storeService.updateStore(id, request, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }
}
