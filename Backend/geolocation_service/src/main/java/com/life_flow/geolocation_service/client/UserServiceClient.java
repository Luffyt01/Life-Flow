package com.life_flow.geolocation_service.client;

import com.life_flow.geolocation_service.dto.user_service.DonorProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.HospitalProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.PageResponse;
import com.life_flow.geolocation_service.dto.user_service.PointDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("/profile/donors/{userId}")
    DonorProfileResponseDto getDonorProfile(@PathVariable("userId") UUID userId);

    @GetMapping("/profile/hospitals/{userId}")
    HospitalProfileResponseDto getHospitalProfile(@PathVariable("userId") UUID userId);

    @GetMapping("/profile/donors/search")
    PageResponse<DonorProfileResponseDto> searchDonors(
            @RequestParam(required = false) String bloodType,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    );

    @GetMapping("/profile/hospitals/search")
    List<HospitalProfileResponseDto> searchHospitals(@RequestParam(required = false) String city);

    @PutMapping("/profile/donors/{userId}/location")
    void updateDonorLocation(@PathVariable("userId") UUID userId, @RequestBody PointDTO locationDto);

    @GetMapping("/profile/donors/nearby")
    List<DonorProfileResponseDto> findNearbyDonors(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("radiusKm") Double radiusKm,
            @RequestParam(value = "bloodType", required = false) String bloodType
    );

    @GetMapping("/profile/hospitals/nearby")
    List<HospitalProfileResponseDto> findNearbyHospitals(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("radiusKm") Double radiusKm
    );
}
