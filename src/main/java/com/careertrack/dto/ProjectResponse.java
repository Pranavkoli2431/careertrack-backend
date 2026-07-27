package com.careertrack.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record ProjectResponse(
        Long id,
        String projectTitle,
        String description,
        String techStack,
        String githubUrl,
        String liveUrl,
        LocalDate startDate,
        LocalDate endDate,
        boolean currentlyWorking,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
