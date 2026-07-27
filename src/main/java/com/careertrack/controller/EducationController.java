package com.careertrack.controller;

import com.careertrack.dto.EducationRequest;
import com.careertrack.dto.EducationResponse;
import com.careertrack.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public ResponseEntity<EducationResponse> createEducation(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EducationRequest request
    ) {
        EducationResponse response =
                educationService.createEducation(getUserId(jwt), request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<EducationResponse> getEducations(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return educationService.getEducations(getUserId(jwt));
    }

    @GetMapping("/{educationId}")
    public EducationResponse getEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long educationId
    ) {
        return educationService.getEducation(
                getUserId(jwt),
                educationId
        );
    }

    @PutMapping("/{educationId}")
    public EducationResponse updateEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long educationId,
            @Valid @RequestBody EducationRequest request
    ) {
        return educationService.updateEducation(
                getUserId(jwt),
                educationId,
                request
        );
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long educationId
    ) {
        educationService.deleteEducation(
                getUserId(jwt),
                educationId
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
