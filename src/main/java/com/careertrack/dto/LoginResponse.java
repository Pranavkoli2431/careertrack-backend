package com.careertrack.dto;

import com.careertrack.entity.UserRole;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        Long userId,
        String fullName,
        String email,
        UserRole role
) {
}
