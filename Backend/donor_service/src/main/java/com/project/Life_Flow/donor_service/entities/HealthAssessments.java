package com.project.Life_Flow.donor_service.entities;

import jakarta.persistence.*;
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
@Table(name = "health_assessments")
public class HealthAssessments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "assessment_id")
    private UUID assessmentId;

    @Column(name = "donor_id", nullable = false)
    private UUID donorId;

    @Column(name = "assessment_date", nullable = false)
    private Date assessmentDate;

    @Column(name = "assessment_type", nullable = false)
    private String assessmentType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "questions", nullable = false, columnDefinition = "jsonb")
    private List<QuestionAnswer> questions;

    @Column(name = "overall_eligibility")
    private Boolean overallEligibility;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "assessed_by_staff_id")
    private UUID assessedByStaffId;

    @Column(name = "next_assessment_due")
    private Date nextAssessmentDue;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionAnswer {
        private String question;
        private String answer;
    }
}
