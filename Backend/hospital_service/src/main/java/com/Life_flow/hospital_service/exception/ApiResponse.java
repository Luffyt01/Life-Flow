package com.Life_flow.hospital_service.exception;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ApiResponse<T> {

    private LocalDate timeStamp;
    private T data;
    private ApiError error;
    public ApiResponse(LocalDate timeStamp) {
        this.timeStamp = LocalDate.now();
    }
    public ApiResponse(LocalDate timestamp, T data) {
        this.timeStamp = timestamp;
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this.error = error;
    }
}
