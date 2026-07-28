package com.careertrack.controller;

import com.careertrack.dto.CertificationRequest;
import com.careertrack.dto.CertificationResponse;
import com.careertrack.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;

    public CertificationController(
            CertificationService certificationService
    ) {
        this.certificationService = certificationService;
    }

    @PostMapping
    public ResponseEntity<CertificationResponse> createCertification(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CertificationRequest request
    ) {
        CertificationResponse response =
                certificationService.createCertification(
                        getUserId(jwt),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<CertificationResponse> getCertifications(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return certificationService.getCertifications(
                getUserId(jwt)
        );
    }

    @GetMapping("/{certificationId}")
    public CertificationResponse getCertification(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long certificationId
    ) {
        return certificationService.getCertification(
                getUserId(jwt),
                certificationId
        );
    }

    @PutMapping("/{certificationId}")
    public CertificationResponse updateCertification(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long certificationId,
            @Valid @RequestBody CertificationRequest request
    ) {
        return certificationService.updateCertification(
                getUserId(jwt),
                certificationId,
                request
        );
    }

    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> deleteCertification(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long certificationId
    ) {
        certificationService.deleteCertification(
                getUserId(jwt),
                certificationId
        );

        return ResponseEntity.noContent().build();
    }

    private Long getUserId(Jwt jwt) {
        Number userId = jwt.getClaim("userId");

        if (userId == null) {
            throw new IllegalStateException(
                    "Authenticated token does not contain userId"
            );
        }

        return userId.longValue();
    }
}
