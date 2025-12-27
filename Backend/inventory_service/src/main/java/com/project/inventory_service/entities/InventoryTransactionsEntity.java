package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.TransactionType;
import com.project.inventory_service.entities.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_transactions",
   indexes = {
        @Index(name="idx_inventory_transactions_bag", columnList = "bagId"),
        @Index(name="idx_inventory_transactions_date", columnList = "createdAt DESC")
   }
)
public class InventoryTransactionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;

    @Column(nullable = false)
    private UUID bagId;

    private UUID fromCenterId;

    private UUID toCenterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusType previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusType newStatus;

    private UUID performedBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private UUID requestId;
    
    private UUID hospitalId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
