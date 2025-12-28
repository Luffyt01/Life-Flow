package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blood_inventory",
       indexes = {
               @Index(name = "idx_blood_inventory_status", columnList = "status, bloodType"),
               @Index(name = "idx_blood_inventory_expiry", columnList = "expiryDate"),
               @Index(name = "idx_blood_inventory_center", columnList = "collectionCenterId, donationDate DESC"),
               @Index(name = "idx_blood_inventory_batch", columnList = "batchNumber")
       }
)
public class BloodInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bagId;

    @Column(nullable = false, length = 5)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(nullable = false, length = 50)
    private String batchNumber;

    @Column(nullable = false)
    private LocalDate donationDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(length = 100)
    private String storageLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusType status = StatusType.AVAILABLE;

    @Column(precision = 4, scale = 1)
    private BigDecimal currentTemperature;

    @Column(nullable = false, unique = true, length = 100)
    private String barcode;

    @Column(length = 100)
    private String rfidTag;

    private UUID appointmentSlotId;

    private UUID donorId;

    @Column(nullable = false)
    private UUID collectionCenterId;

    @Enumerated(EnumType.STRING)
    private QualityCheckStatus qualityCheckStatus;
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;

    @Column(nullable = false)
    private Integer volumeMl;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private BloodComponentType componentType;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
