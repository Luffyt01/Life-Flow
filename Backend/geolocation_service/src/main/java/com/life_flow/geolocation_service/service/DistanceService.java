package com.life_flow.geolocation_service.service;


import com.life_flow.geolocation_service.dto.osrm.OsrmResponseDTO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;

@Slf4j
@Service
public class DistanceService {

    private static final String OSRM_API_BASE_URL =
            "https://router.project-osrm.org/route/v1/driving/";

    private final RestClient restClient = RestClient.builder()
            .baseUrl(OSRM_API_BASE_URL)
            .defaultHeader("Accept", "application/json")
            .defaultHeader("Accept-Encoding", "identity") // 🔥 FIX
            .build();

    public double calculateDistance(Point src, Point dest) {

        String coordinates =
                src.getX() + "," + src.getY() + ";" +
                        dest.getX() + "," + dest.getY();
        log.info("coordinates {}",coordinates);
        log.info("url",OSRM_API_BASE_URL+coordinates);


        OsrmResponseDTO response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(coordinates)
                        .queryParam("overview", "false")
                        .build())
                .retrieve()
                .body(OsrmResponseDTO.class);

        if (response != null &&
                response.getRoutes() != null &&
                !response.getRoutes().isEmpty()) {

            return response.getRoutes().get(0).getDistance() / 1000.0;
        }

        return 0.0;
    }
}


