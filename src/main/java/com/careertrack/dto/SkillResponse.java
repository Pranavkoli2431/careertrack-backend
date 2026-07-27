package com.careertrack.dto;

import com.careertrack.entity.SkillProficiency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record SkillResponse(
        Long id,
        String skillName,
        SkillProficiency proficiency,
        BigDecimal yearsOfExperience,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
