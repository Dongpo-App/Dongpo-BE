package com.dongyang.dongpo.controller.location;

import com.dongyang.dongpo.apiresponse.ApiResponse;
import com.dongyang.dongpo.dto.location.LatLongComparisonDto;
import com.dongyang.dongpo.service.location.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/api/location/distance")
    public ResponseEntity<ApiResponse<Long>> getDistance(@RequestBody LatLongComparisonDto latLongComparison) {
        return ResponseEntity.ok(new ApiResponse<>(locationService.getDistance(latLongComparison)));
    }

    @PostMapping("/api/location/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyCoordinate(@RequestBody LatLongComparisonDto latLongComparison) {
        return (locationService.verifyCoordinate(latLongComparison)
                ? ResponseEntity.ok(new ApiResponse<>(true))
                : ResponseEntity.ok(new ApiResponse<>(false)));
    }
}
