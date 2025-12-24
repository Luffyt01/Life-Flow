package com.Life_flow.hospital_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HospitalResponseDTO {
    private UUID hospitalId;
    private UUID userId;
    private String hospitalName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String contactPhone;
    private String emergencyContact;
    private Integer bloodBankCapacity;
    private String operatingHoursStart;
    private String operatingHoursEnd;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}