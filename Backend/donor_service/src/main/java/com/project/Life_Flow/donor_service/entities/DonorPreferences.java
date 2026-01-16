package com.project.Life_Flow.donor_service.entities;

import com.project.Life_Flow.donor_service.entities.enums.CommunicationFrequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donor_preferences")
public class DonorPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "preference_id")
    private UUID preferenceId;

    @Column(name = "donor_id", unique = true, nullable = false)
    private UUID donorId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "notification_preferences", columnDefinition = "jsonb")
    private NotificationPrefs notificationPreferences;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "privacy_settings", columnDefinition = "jsonb")
    private PrivacySettings privacySettings;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_frequency")
    private CommunicationFrequency communicationFrequency;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationPrefs {
        private boolean email;
        private boolean sms;
        private boolean push;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrivacySettings {
        private boolean showName;
        private boolean showLocation;
    }
}
