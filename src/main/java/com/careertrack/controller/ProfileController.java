package com.careertrack.controller;

import com.careertrack.dto.ProfileResponse;
import com.careertrack.dto.ProfileUpdateRequest;
import com.careertrack.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserProfileService userProfileService;

    public ProfileController(
            UserProfileService userProfileService
    ) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public ProfileResponse getMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return userProfileService.getProfile(
                getUserId(jwt)
        );
    }

    @PutMapping("/me")
    public ProfileResponse createOrUpdateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ProfileUpdateRequest request
    ) {
        return userProfileService.createOrUpdateProfile(
                getUserId(jwt),
                request
        );
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
