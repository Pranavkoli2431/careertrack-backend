package com.careertrack.dto;

import java.time.OffsetDateTime;

public record ProfileResponse(
        Long profileId,
        Long userId,
        String fullName,
        String email,
        String headline,
        String bio,
        String phone,
        String city,
        String state,
        String country,
        String desiredRole,
        String linkedinUrl,
        String githubUrl,
        String portfolioUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
