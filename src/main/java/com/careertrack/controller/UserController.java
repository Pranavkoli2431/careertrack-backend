package com.careertrack.controller;

import com.careertrack.dto.CurrentUserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public CurrentUserResponse getCurrentUser(
            @AuthenticationPrincipal Jwt jwt
    ) {
        Number userId = jwt.getClaim("userId");

        return new CurrentUserResponse(
                userId.longValue(),
                jwt.getClaimAsString("fullName"),
                jwt.getSubject(),
                jwt.getClaimAsStringList("roles")
        );
    }
}
