package com.project.inventory_service.entities;

import com.project.inventory_service.entities.enums.TransactionType;
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
        @Index(name="bag_id_idx",columnList = "bagId"),
        @Index(name="transaction_type_idx",columnList = "transactionType"),
        @Index(name="transaction_date_idx",columnList = "transactionDate"),
        @Index(name="request_id_idx",columnList = "requestId")
   }
)
public class InventoryTransactionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transactionId;


    private UUID bagId;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private TransactionType transactionType;

    @Column( nullable = false)
    private int  quantity=1;


    @Column( nullable = false,length = 255)
    private String fromLocation;

    @Column( nullable = false,length = 255)
    private String toLocation;


    private UUID requestId;

    @Column(length = 255)
    private String performBy;

    private String notes;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime transactionDate;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

}
