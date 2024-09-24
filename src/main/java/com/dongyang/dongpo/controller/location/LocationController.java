package com.dongyang.dongpo.controller.location;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.dto.location.LatLongComparisonDto;
import com.dongyang.dongpo.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/distance")
    public ResponseEntity<ApiResponse<Long>> getDistance(@RequestBody LatLongComparisonDto latLongComparison) {
        return ResponseEntity.ok(new ApiResponse<>(locationService.getDistance(latLongComparison)));
    }

    @PostMapping("/verify") // 방문 인증 기능
    public ResponseEntity<ApiResponse<Boolean>> verifyVisitCert(@RequestBody LatLongComparisonDto latLongComparison,
                                                                @AuthenticationPrincipal Member member) {
        return (locationService.verifyVisitCert(latLongComparison, member)
                ? ResponseEntity.ok(new ApiResponse<>(true))
                : ResponseEntity.ok(new ApiResponse<>(false)));
    }
}
