package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.BadgeLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gamification")
public class Gamification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "gamification_id")
    private UUID gamificationId;

    @Column(name = "donor_id", unique = true, nullable = false)
    private UUID donorId;

    @Min(0)
    @Column(name = "total_points")
    private Integer totalPoints;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_level")
    private BadgeLevel badgeLevel;

    @Column(name = "donations_count")
    private Integer donationsCount;

    @Column(name = "emergency_responses")
    private Integer emergencyResponses;

    @Column(name = "lives_saved_estimate")
    private Integer livesSavedEstimate;

    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Column(name = "streak_days")
    private Integer streakDays;

    @Column(name = "last_donation_date")
    private Date lastDonationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "achievements", columnDefinition = "jsonb")
    private List<Achievement> achievements;

    @Column(name = "referral_count")
    private Integer referralCount;

    @Column(name = "ranking_position")
    private Integer rankingPosition;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "points_history", columnDefinition = "jsonb")
    private List<PointsHistory> pointsHistory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Achievement {
        private String achievementName;
        private Date dateAwarded;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointsHistory {
        private Integer points;
        private String reason;
        private Timestamp date;
    }
}
