package com.careertrack.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record EducationResponse(
        Long id,
        String institutionName,
        String degree,
        String fieldOfStudy,
        LocalDate startDate,
        LocalDate endDate,
        boolean currentlyStudying,
        String grade,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
