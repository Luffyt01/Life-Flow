package com.project.inventory_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "collection_centers",
       indexes = {
           @Index(name = "idx_center_location", columnList = "latitude, longitude"),
           @Index(name = "idx_center_hospital", columnList = "hospitalId"),
           @Index(name = "idx_center_active", columnList = "isActive")
       }
)
public class CollectionCenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID centerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(columnDefinition = "INTEGER DEFAULT 4")
    private Integer capacityPerHour = 4;

    @Column(nullable = false)
    private LocalTime operatingHoursStart;

    @Column(nullable = false)
    private LocalTime operatingHoursEnd;

    @Column(columnDefinition = "INTEGER DEFAULT 2")
    private Integer staffCount = 2;

    @Column(length = 20)
    private String contactNumber;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String equipmentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
