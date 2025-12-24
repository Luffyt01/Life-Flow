package com.Life_flow.hospital_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "hospitals")
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hospitalId;

//    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String hospitalName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String state;

    @Column(nullable = false, length = 10)
    private String pincode;

    @Column(name = "contact_phone", nullable = false, length = 20)
    private String contactPhone;

    @Column(name = "emergency_contact", nullable = false, length = 20)
    private String emergencyContact;

    @Column(name = "blood_bank_capacity")
    private Integer bloodBankCapacity;

    @Column(name = "operating_hours_start", nullable = false, length = 5)
    private String operatingHoursStart;

    @Column(name = "operating_hours_end", nullable = false, length = 5)
    private String operatingHoursEnd;

    @Column(columnDefinition = "DECIMAL(10,6)")
    private Double latitude;

    @Column(columnDefinition = "DECIMAL(10,6)")
    private Double longitude;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}