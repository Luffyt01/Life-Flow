package com.life_flow.geolocation_service.dto.osrm;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmRoute {
    private double distance;
}

