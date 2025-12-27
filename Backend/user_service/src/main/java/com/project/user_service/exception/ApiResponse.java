package com.project.user_service.exception;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiResponse<T> {
    private LocalDate timeStamp;
    private T data;
    private ApiError error;

    public ApiResponse(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ApiResponse(LocalDate timeStamp, T data) {
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this.error = error;
    }
}
