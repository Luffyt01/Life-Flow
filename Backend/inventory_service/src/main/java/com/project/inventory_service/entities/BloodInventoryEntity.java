package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
               @Index(name = "blood_type_idx", columnList = "bloodType"),
               @Index(name = "status_idx", columnList = "status"),
               @Index(name = "donor_id_idx", columnList = "donorId"),
               @Index(name = "expairy_date_idx", columnList = "expiryDate"),
       }

)

public class BloodInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bagId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private String batchNumber;

    @CreationTimestamp
    private LocalDate donationDate;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate collectionDate;
    @Column(nullable = false)
    private LocalDate expiryDate;

    private String storageLocation;

    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.AVAILABLE;

    private UUID reservedForRequestId;

    private LocalDate reservedAt;


    private UUID reserveByHospitalID;


    private UUID donorId;

    @Enumerated(EnumType.STRING)
    private BloodComponentType bloodComponentType = BloodComponentType.WHOLE_BLOOD;

    private Double unitsAvailable;

    @Enumerated(EnumType.STRING)
    private QualityCheckStatus qualityCheckStatus = QualityCheckStatus.PENDING;

    private LocalDate qualityCheckDate;

    private String qualityCheckNotes;


    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;




}
