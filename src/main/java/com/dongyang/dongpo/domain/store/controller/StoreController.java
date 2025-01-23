package com.dongyang.dongpo.domain.store.controller;

import com.dongyang.dongpo.common.dto.apiresponse.ApiResponse;
import com.dongyang.dongpo.common.dto.location.LatLong;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.store.dto.*;
import com.dongyang.dongpo.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store API", description = "점포 관련 API")
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @GetMapping("")
    @Operation(summary = "현재 위치 기준 주변 점포 조회")
    public ResponseEntity<ApiResponse<List<NearbyStoresResponseDto>>> getStoresByCurrentLocation(@Valid @ModelAttribute final LatLong latLong,
                                                                                                 @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.findStoresByCurrentLocation(latLong, member)));
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "점포 간략 정보 조회")
    public ResponseEntity<ApiResponse<StoreBasicInfoResponseDto>> getStoreBasicInfo(@PathVariable @Min(1) final Long id,
                                                                                    @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.getStoreBasicInfo(id, member)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "점포 상세 조회")
    public ResponseEntity<ApiResponse<StoreDetailInfoResponseDto>> getStoreDetailInfo(@PathVariable @Min(1) final Long id,
                                                                                      @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.getStoreDetailInfo(id, member)));
    }

    @PostMapping("")
    @Operation(summary = "점포 등록")
    public ResponseEntity<Void> addStore(@Valid @RequestBody final StoreRegisterDto request,
                                         @AuthenticationPrincipal final Member member) {
        storeService.addStore(request, member);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "점포 삭제")
    public ResponseEntity<Void> deleteStore(@PathVariable @Min(1) final Long id,
                                            @AuthenticationPrincipal final Member member) {
        storeService.deleteStore(id, member);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "점포 정보 수정")
    public ResponseEntity<Void> updateStore(@PathVariable @Min(1) final Long id,
                                            @Valid @RequestBody final StoreInfoUpdateDto request,
                                            @AuthenticationPrincipal final Member member) {
        storeService.updateStoreInfo(id, request, member);
        return ResponseEntity.ok().build();
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

    @PostMapping("/{storeId}/visit-cert")
    @Operation(summary = "점포 방문 인증")
    public ResponseEntity<Void> visitCert(@PathVariable @Min(1) final Long storeId,
                                          @RequestBody final StoreVisitCertDto storeVisitCertDto,
                                          @AuthenticationPrincipal final Member member) {
        storeService.visitCert(storeId, storeVisitCertDto, member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{storeId}/visit-cert/check")
    @Operation(summary = "24시간 이내 방문 인증 여부 조회")
    public ResponseEntity<ApiResponse<Boolean>> checkVisitCertBy24Hours(@PathVariable @Min(1) final Long storeId,
                                                                        @AuthenticationPrincipal final Member member) {
        return ResponseEntity.ok(new ApiResponse<>(storeService.checkVisitCertBy24Hours(storeId, member)));
    }
}
