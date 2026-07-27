package com.careertrack.dto;

import com.careertrack.entity.UserRole;

import java.time.OffsetDateTime;

public record RegisterResponse(
        Long id,
        String fullName,
        String email,
        UserRole role,
        boolean active,
        OffsetDateTime createdAt
) {
}
