package com.careertrack.controller;

import com.careertrack.dto.SkillRequest;
import com.careertrack.dto.SkillResponse;
import com.careertrack.service.UserSkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    private final UserSkillService userSkillService;

    public SkillController(
            UserSkillService userSkillService
    ) {
        this.userSkillService = userSkillService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> addSkill(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SkillRequest request
    ) {
        SkillResponse response = userSkillService.addSkill(
                getUserId(jwt),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<SkillResponse> getMySkills(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return userSkillService.getSkills(getUserId(jwt));
    }

    @PutMapping("/{skillId}")
    public SkillResponse updateSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long skillId,
            @Valid @RequestBody SkillRequest request
    ) {
        return userSkillService.updateSkill(
                getUserId(jwt),
                skillId,
                request
        );
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> deleteSkill(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long skillId
    ) {
        userSkillService.deleteSkill(
                getUserId(jwt),
                skillId
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
