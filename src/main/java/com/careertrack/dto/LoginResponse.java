package com.careertrack.dto;

import com.careertrack.entity.UserRole;

public record LoginResponse(
        Long id,
        String fullName,
        String email,
        UserRole role,
        String message
) {
}
