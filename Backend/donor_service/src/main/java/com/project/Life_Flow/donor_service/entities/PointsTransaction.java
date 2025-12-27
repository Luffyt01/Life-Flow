package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "points_transactions",
        indexes = {
                @Index(name = "idx_points_donor_id", columnList = "donor_id"),
                @Index(name = "idx_transaction_date", columnList = "transactionDate")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointsTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private DonorProfile donor;

    private Integer pointsEarned;
    private Integer pointsRedeemed;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(length = 500)
    private String description;

    private String relatedRequestId;

    @CreationTimestamp
    private LocalDateTime transactionDate;
}
