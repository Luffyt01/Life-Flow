package com.life_flow.geolocation_service.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CalculateSlotsRequest {
    private String center_id;
    private List<String> available_donors;
    private String time_slot_start;
    private String time_slot_end;
    private int required_units;
    private int slot_duration;
    private Map<String, Object> constraints;
}
