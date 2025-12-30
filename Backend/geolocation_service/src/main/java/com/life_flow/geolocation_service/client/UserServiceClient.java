package com.life_flow.geolocation_service.client;

import com.life_flow.geolocation_service.dto.user_service.DonorProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.HospitalProfileResponseDto;
import com.life_flow.geolocation_service.dto.user_service.PageResponse;
import com.life_flow.geolocation_service.dto.user_service.PointDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(RestClient.Builder builder, @Value("${user-service.url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public DonorProfileResponseDto getDonorProfile(UUID userId) {
        return restClient.get()
                .uri("/profile/donors/{userId}", userId)
                .retrieve()
                .body(DonorProfileResponseDto.class);
    }

    public HospitalProfileResponseDto getHospitalProfile(UUID userId) {
        return restClient.get()
                .uri("/profile/hospitals/{userId}", userId)
                .retrieve()
                .body(HospitalProfileResponseDto.class);
    }

    public PageResponse<DonorProfileResponseDto> searchDonors(String bloodType, String city, int page, int limit) {
        return restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/profile/donors/search");
                    if (bloodType != null) {
                        builder.queryParam("bloodType", bloodType);
                    }
                    if (city != null) {
                        builder.queryParam("city", city);
                    }
                    return builder.queryParam("page", page)
                            .queryParam("limit", limit)
                            .build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<PageResponse<DonorProfileResponseDto>>() {});
    }

    public List<HospitalProfileResponseDto> searchHospitals(String city) {
        return restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/profile/hospitals/search");
                    if (city != null) {
                        builder.queryParam("city", city);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<HospitalProfileResponseDto>>() {});
    }

    public void updateDonorLocation(UUID userId, PointDTO locationDto) {
        restClient.put()
                .uri("/profile/donors/{userId}/location", userId)
                .body(locationDto)
                .retrieve()
                .toBodilessEntity();
    }

    public List<DonorProfileResponseDto> findNearbyDonors(Double latitude, Double longitude, Double radiusKm, String bloodType) {
        return restClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/profile/donors/nearby")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("radiusKm", radiusKm);
                    if (bloodType != null) {
                        builder.queryParam("bloodType", bloodType);
                    }
                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<List<DonorProfileResponseDto>>() {});
    }

    public List<HospitalProfileResponseDto> findNearbyHospitals(Double latitude, Double longitude, Double radiusKm) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/profile/hospitals/nearby")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("radiusKm", radiusKm)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<HospitalProfileResponseDto>>() {});
    }
}
