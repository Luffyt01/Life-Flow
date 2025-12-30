package com.life_flow.geolocation_service.service;

import com.life_flow.geolocation_service.dto.osrm.OsrmResponseDTO;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DistanceService {

    private static final String OSRM_API_BASE_URL = "http://router.project-osrm.org/route/v1/driving/";

    public double calculateDistance(Point src, Point dest) {
        try {
            // OSRM expects coordinates in longitude,latitude format
            String coordinates = src.getX() + "," + src.getY() + ";" + dest.getX() + "," + dest.getY();

            OsrmResponseDTO responseDTO = RestClient.builder()
                    .baseUrl(OSRM_API_BASE_URL)
                    .build()
                    .get()
                    .uri(coordinates)
                    .retrieve()
                    .body(OsrmResponseDTO.class);

            if (responseDTO != null && responseDTO.getRoutes() != null && !responseDTO.getRoutes().isEmpty()) {
                return responseDTO.getRoutes().get(0).getDistance() / 1000.0; // Convert meters to kilometers
            }
            throw new RuntimeException("No routes found in OSRM response");
        } catch (Exception e) {
            throw new RuntimeException("Error getting data from OSRM: " + e.getMessage());
        }
    }
}
