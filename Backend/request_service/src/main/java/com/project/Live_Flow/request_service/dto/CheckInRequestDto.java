package com.project.Live_Flow.request_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequestDto {

    @NotBlank(message = "Confirmation code cannot be blank")
    private String confirmationCode;

    @NotNull(message = "Staff ID cannot be null")
    private UUID staffId;
}
