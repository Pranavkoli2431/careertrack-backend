package com.careertrack.dto;

import com.careertrack.entity.EmploymentType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ExperienceResponse(
        Long id,
        String companyName,
        String jobTitle,
        EmploymentType employmentType,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        boolean currentlyWorking,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
