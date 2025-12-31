package com.life_flow.geolocation_service.dto.osrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmResponseDTO {
    private List<OsrmRoute> routes;
}
