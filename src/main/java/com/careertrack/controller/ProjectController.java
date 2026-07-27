package com.careertrack.controller;

import com.careertrack.dto.ProjectRequest;
import com.careertrack.dto.ProjectResponse;
import com.careertrack.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ProjectRequest request
    ) {
        ProjectResponse response =
                projectService.createProject(
                        getUserId(jwt),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<ProjectResponse> getProjects(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return projectService.getProjects(
                getUserId(jwt)
        );
    }

    @GetMapping("/{projectId}")
    public ProjectResponse getProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long projectId
    ) {
        return projectService.getProject(
                getUserId(jwt),
                projectId
        );
    }

    @PutMapping("/{projectId}")
    public ProjectResponse updateProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request
    ) {
        return projectService.updateProject(
                getUserId(jwt),
                projectId,
                request
        );
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(
                getUserId(jwt),
                projectId
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
