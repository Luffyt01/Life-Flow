package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blood_inventory",
       indexes = {
               @Index(name = "blood_type_idx", columnList = "bloodType"),
               @Index(name = "status_idx", columnList = "status"),
               @Index(name = "donor_id_idx", columnList = "donorId"),
               @Index(name = "expairy_date_idx", columnList = "expiryDate"),
               @Index(name = "batch_number_idx", columnList = "batchNumber")
       }

)

public class BloodInventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bagId;

    @Column(name ="blood_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    @Column(nullable = false,name = "batch_number")
    private String batchNumber;

    @Column( nullable = false)
    private LocalDate donationDate;

    private LocalDate collectionDate;

    private LocalDate expiryDate;

    private String storageLocation;

    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.AVAILABLE;

    private UUID reservedForRequestId;

    private LocalDate reservedAt;


    private UUID reserveByHospitalID;

    @Column(nullable = false)
    private UUID donorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_component_type",nullable = false)
    private BloodComponentType bloodComponentType = BloodComponentType.WHITE_BLOOD;

    private Double unitsAvailable;

    private Double unitCost;

    private QualityCheckStatus qualityCheckStatus = QualityCheckStatus.PENDING;

    private LocalDate qualityCheckDate;

    @Column(name = "quality_check_notes",length = 255)
    private String qualityCheckNotes;


    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    // Check if the blood bag is expired
    public boolean isExpired() {
        return expiryDate != null && LocalDate.now().isAfter(expiryDate);
    }

    // Check if the blood bag is available for use
    public boolean isAvailable() {
        return status == StatusType.AVAILABLE && !isExpired();
    }

    // Reserve this blood bag for a specific request
    public boolean reserveForRequest(UUID requestId, UUID hospitalId) {
        if (isAvailable()) {
            this.status = StatusType.RESERVED;
            this.reservedForRequestId = requestId;
            this.reserveByHospitalID = hospitalId;
            this.reservedAt = LocalDate.now();
            return true;
        }
        return false;
    }

    // Release reservation on this blood bag
    public void releaseReservation() {
        if (status == StatusType.RESERVED) {
            this.status = StatusType.AVAILABLE;
            this.reservedForRequestId = null;
            this.reserveByHospitalID = null;
            this.reservedAt = null;
        }
    }

    // Mark this blood bag as used
    public void markAsUsed() {
        this.status = StatusType.USED;
    }

    // Update quality check information
    public void updateQualityCheck(QualityCheckStatus status, String notes) {
        this.qualityCheckStatus = status;
        this.qualityCheckNotes = notes;
        this.qualityCheckDate = LocalDate.now();

        if (status == QualityCheckStatus.FAIL) {
            this.status = StatusType.DISCARDED;
        }
    }

    // Check if this blood bag is suitable for a patient
    public boolean isCompatibleWith(BloodType patientBloodType) {
        // Implement blood type compatibility logic here
        // Example: O- can donate to anyone, AB+ can receive from anyone
        // This is a simplified version - you'll need to implement the full logic
        return patientBloodType != null &&
                (this.bloodType == BloodType.O_NEG ||
                        this.bloodType == patientBloodType);
    }

    // Get the age of the blood bag in days
    public long getAgeInDays() {
        return donationDate.until(LocalDate.now()).getDays();
    }

    // Check if the blood bag is near expiration
    public boolean isNearExpiration(int daysThreshold) {
        return expiryDate != null &&
                LocalDate.now().plusDays(daysThreshold).isAfter(expiryDate);
    }
}
