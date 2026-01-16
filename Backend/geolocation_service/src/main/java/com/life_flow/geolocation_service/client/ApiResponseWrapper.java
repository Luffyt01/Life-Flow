package com.life_flow.geolocation_service.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ApiResponseWrapper<T> {
    private T data;
    private String error;

    @JsonProperty("timeStamp")
    private LocalDate timeStamp;
}

// For List of DonorProfileResponseDto
