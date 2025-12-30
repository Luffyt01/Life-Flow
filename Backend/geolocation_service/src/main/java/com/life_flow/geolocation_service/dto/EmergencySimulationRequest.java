package com.life_flow.geolocation_service.dto;

import lombok.Data;

@Data
public class EmergencySimulationRequest {
    private String center_id;
    private String blood_type;
    private int units_required;
    private String time_of_day;
    private String day_of_week;
}
