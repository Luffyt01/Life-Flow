package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateLocationResponse {
    private boolean location_updated;
    private boolean geofence_check;
    private List<String> nearby_requests;
}
