package com.dongyang.dongpo.service.location;

import com.dongyang.dongpo.dto.location.CoordinateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocationServiceTest {
    LocationService locationService;

    @BeforeEach
    public void beforeEach() {
        locationService = new LocationService();
    }

    @Test
    void calcDistance() {
        CoordinateDto newCrd = CoordinateDto.builder().lat(37.506609).lng(126.890552).build();
        CoordinateDto targetCrd = CoordinateDto.builder().lat(37.500278).lng(126.868058).build();

        Long result = locationService.calcDistance(newCrd, targetCrd);
        System.out.println("result = " + result + "m");
    }
}