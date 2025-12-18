package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "donor_gamification",
        indexes = {
                @Index(name = "idx_gamification_donor_id", columnList = "donor_id"),
                @Index(name = "idx_badge_level", columnList = "badgeLevel"),
                @Index(name = "idx_top_donor_rank", columnList = "topDonorRank")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonorGamification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID gamificationId;

    @OneToOne
    @JoinColumn(name = "donor_id", unique = true)
    private DonorProfile donor;

    private Integer totalPoints;
    private Integer totalDonations;
    private Integer estimatedLivesSaved;

    @Enumerated(EnumType.STRING)
    private BadgeLevel badgeLevel;

    @Lob
    private String achievementMilestones; // JSON

    private Integer heroOfMonthCount;
    private Integer heroOfYearCount;
    private Integer topDonorRank;
    private Integer loyaltyScore;
    private Integer referralCount;
    private LocalDate lastDonationDate;
    private Integer consecutiveDonations;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}

