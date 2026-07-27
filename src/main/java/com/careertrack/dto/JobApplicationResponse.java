package com.careertrack.dto;

import com.careertrack.entity.ApplicationStatus;
import com.careertrack.entity.EmploymentType;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record JobApplicationResponse(
        Long id,
        String companyName,
        String jobTitle,
        String jobUrl,
        String location,
        EmploymentType employmentType,
        ApplicationStatus applicationStatus,
        LocalDate appliedDate,
        LocalDate deadline,
        String notes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
