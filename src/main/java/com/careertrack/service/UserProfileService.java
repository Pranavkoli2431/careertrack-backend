package com.careertrack.service;

import com.careertrack.dto.ProfileResponse;
import com.careertrack.dto.ProfileUpdateRequest;
import com.careertrack.entity.User;
import com.careertrack.entity.UserProfile;
import com.careertrack.repository.UserProfileRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Long userId) {

        UserProfile profile = userProfileRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User profile has not been created yet"
                ));

        return toResponse(profile);
    }

    @Transactional
    public ProfileResponse createOrUpdateProfile(
            Long userId,
            ProfileUpdateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        UserProfile profile = userProfileRepository
                .findByUser_Id(userId)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        profile.setHeadline(clean(request.getHeadline()));
        profile.setBio(clean(request.getBio()));
        profile.setPhone(clean(request.getPhone()));
        profile.setCity(clean(request.getCity()));
        profile.setState(clean(request.getState()));
        profile.setCountry(clean(request.getCountry()));
        profile.setDesiredRole(clean(request.getDesiredRole()));
        profile.setLinkedinUrl(clean(request.getLinkedinUrl()));
        profile.setGithubUrl(clean(request.getGithubUrl()));
        profile.setPortfolioUrl(clean(request.getPortfolioUrl()));

        UserProfile savedProfile =
                userProfileRepository.save(profile);

        return toResponse(savedProfile);
    }

    private ProfileResponse toResponse(UserProfile profile) {

        User user = profile.getUser();

        return new ProfileResponse(
                profile.getId(),
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                profile.getHeadline(),
                profile.getBio(),
                profile.getPhone(),
                profile.getCity(),
                profile.getState(),
                profile.getCountry(),
                profile.getDesiredRole(),
                profile.getLinkedinUrl(),
                profile.getGithubUrl(),
                profile.getPortfolioUrl(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty() ? null : trimmedValue;
    }
}
