package com.careertrack.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record CertificationResponse(
        Long id,
        String certificateName,
        String issuingOrganization,
        LocalDate issueDate,
        LocalDate expirationDate,
        boolean doesNotExpire,
        String credentialId,
        String credentialUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
