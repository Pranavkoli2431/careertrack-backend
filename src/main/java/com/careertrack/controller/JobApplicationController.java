package com.careertrack.controller;

import com.careertrack.dto.JobApplicationRequest;
import com.careertrack.dto.JobApplicationResponse;
import com.careertrack.entity.ApplicationStatus;
import com.careertrack.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService
    ) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> createApplication(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse response =
                jobApplicationService.createApplication(
                        getUserId(jwt),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<JobApplicationResponse> getApplications(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false)
            ApplicationStatus status
    ) {
        return jobApplicationService.getApplications(
                getUserId(jwt),
                status
        );
    }

    @GetMapping("/{applicationId}")
    public JobApplicationResponse getApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long applicationId
    ) {
        return jobApplicationService.getApplication(
                getUserId(jwt),
                applicationId
        );
    }

    @PutMapping("/{applicationId}")
    public JobApplicationResponse updateApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long applicationId,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return jobApplicationService.updateApplication(
                getUserId(jwt),
                applicationId,
                request
        );
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long applicationId
    ) {
        jobApplicationService.deleteApplication(
                getUserId(jwt),
                applicationId
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
