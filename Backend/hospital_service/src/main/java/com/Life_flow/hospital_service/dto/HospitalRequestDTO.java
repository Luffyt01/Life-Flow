package com.Life_flow.hospital_service.dto;

import lombok.Data;


@Data
public class HospitalRequestDTO {
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
}