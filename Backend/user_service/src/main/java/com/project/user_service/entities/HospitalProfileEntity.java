package com.project.user_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hospital_profiles",
        indexes = {
                @Index(name = "idx_hospital_location", columnList = "location")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalProfileEntity {

    @Id
    @Column(name = "hospital_id")
    private UUID hospitalId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "hospital_id")
    private UserEntity user;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String licenseNumber;

    private String documentDriveUrl;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition= "Geometry(Point, 4326)")
    private Point location;

    private String contactPerson;

    @Column( length = 20)
    private String emergencyContact;

    private boolean isVerified;

    private LocalDateTime verificationDate;

    private String description;
    private String website;

    @CreationTimestamp
    @Column( updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
