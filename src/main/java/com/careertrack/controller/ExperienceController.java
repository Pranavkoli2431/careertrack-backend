package com.careertrack.controller;

import com.careertrack.dto.ExperienceRequest;
import com.careertrack.dto.ExperienceResponse;
import com.careertrack.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(
            ExperienceService experienceService
    ) {
        this.experienceService = experienceService;
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> createExperience(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ExperienceRequest request
    ) {
        ExperienceResponse response =
                experienceService.createExperience(
                        getUserId(jwt),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<ExperienceResponse> getExperiences(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return experienceService.getExperiences(
                getUserId(jwt)
        );
    }

    @GetMapping("/{experienceId}")
    public ExperienceResponse getExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long experienceId
    ) {
        return experienceService.getExperience(
                getUserId(jwt),
                experienceId
        );
    }

    @PutMapping("/{experienceId}")
    public ExperienceResponse updateExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long experienceId,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return experienceService.updateExperience(
                getUserId(jwt),
                experienceId,
                request
        );
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Void> deleteExperience(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long experienceId
    ) {
        experienceService.deleteExperience(
                getUserId(jwt),
                experienceId
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
