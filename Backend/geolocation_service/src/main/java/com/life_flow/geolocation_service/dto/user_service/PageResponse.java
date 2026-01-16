package com.life_flow.geolocation_service.dto.user_service;

import lombok.Data;
import java.util.List;

@Data
public class PageResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
}
