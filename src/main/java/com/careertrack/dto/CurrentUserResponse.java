package com.careertrack.dto;

import java.util.List;

public record CurrentUserResponse(
        Long userId,
        String fullName,
        String email,
        List<String> roles
) {
}
