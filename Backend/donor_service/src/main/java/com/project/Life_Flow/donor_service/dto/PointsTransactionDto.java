package com.project.Life_Flow.donor_service.dto;

import com.project.Life_Flow.donor_service.entities.DonorProfile;
import com.project.Life_Flow.donor_service.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsTransactionDto {

    private UUID transactionId;
    private UUID donor;
    private Integer pointsEarned;
    private Integer pointsRedeemed;
    private TransactionType transactionType;
    private String description;
    private String relatedRequestId;
    private LocalDateTime transactionDate;
}
