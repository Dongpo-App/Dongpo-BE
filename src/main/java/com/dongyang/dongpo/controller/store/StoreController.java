package com.dongyang.dongpo.controller.store;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.location.LatLong;
import com.dongyang.dongpo.dto.store.*;
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

    @GetMapping("")
    @Operation(summary = "현재 위치 기준 주변 점포 조회")
    public ResponseEntity<ApiResponse<List<StoreSummaryDto>>> getStoresByCurrentLocation(@ModelAttribute LatLong latLong,
                                                                                         @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.findStoresByCurrentLocation(latLong, member)));
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "점포 간략 정보 조회")
    public ResponseEntity<ApiResponse<StoreSummaryDto>> getStoreSummary(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.getStoreSummary(id, member)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "점포 상세 조회")
    public ResponseEntity<ApiResponse<StoreDto>> detailStore(@PathVariable Long id, @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.detailStore(id, member)));
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

    @GetMapping("/recommend/age")
    @Operation(summary = "연령대별 추천 점포 조회")
    public ResponseEntity<ApiResponse<RecommendResponse>> recommendStoreByAge(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.recommendStoreByAge(member)));
    }

    @GetMapping("/recommend/gender")
    @Operation(summary = "성별 추천 점포 조회")
    public ResponseEntity<ApiResponse<RecommendResponse>> recommendStoreBySex(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.recommendStoreByGender(member)));
    }

    @PostMapping("/visit-cert")
    @Operation(summary = "점포 방문 인증")
    public ResponseEntity<ApiResponse<String>> visitCert(@RequestBody StoreVisitCertDto storeVisitCertDto,
                                                         @AuthenticationPrincipal Member member) {
        storeService.visitCert(storeVisitCertDto, member);
        return ResponseEntity.ok(new ApiResponse<>("success"));
    }

    @GetMapping("/visit-cert/{storeId}")
    @Operation(summary = "24시간 이내 방문 인증 여부 조회")
    public ResponseEntity<ApiResponse<Boolean>> checkVisitCertBy24Hours(@PathVariable Long storeId,
                                                                        @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.checkVisitCertBy24Hours(storeId, member)));
    }
}
