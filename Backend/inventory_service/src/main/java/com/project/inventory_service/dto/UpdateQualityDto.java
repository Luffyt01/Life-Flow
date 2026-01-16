package com.project.inventory_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate qualityCheckDate;
    private String qualityCheckNotes;
}
