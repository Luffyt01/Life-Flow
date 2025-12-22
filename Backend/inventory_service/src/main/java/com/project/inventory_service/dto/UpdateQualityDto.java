package com.project.inventory_service.dto;

import com.project.inventory_service.entities.enums.QualityCheckStatus;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class UpdateQualityDto {
    private QualityCheckStatus qualityCheckStatus;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in yyyy-MM-dd format")
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;
}
