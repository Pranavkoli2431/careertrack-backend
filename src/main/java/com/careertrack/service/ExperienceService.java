package com.careertrack.service;

import com.careertrack.dto.ExperienceRequest;
import com.careertrack.dto.ExperienceResponse;
import com.careertrack.entity.User;
import com.careertrack.entity.UserExperience;
import com.careertrack.repository.UserExperienceRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExperienceService {

    private final UserRepository userRepository;
    private final UserExperienceRepository experienceRepository;

    public ExperienceService(
            UserRepository userRepository,
            UserExperienceRepository experienceRepository
    ) {
        this.userRepository = userRepository;
        this.experienceRepository = experienceRepository;
    }

    @Transactional
    public ExperienceResponse createExperience(
            Long userId,
            ExperienceRequest request
    ) {
        validateDates(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        UserExperience experience = new UserExperience();
        experience.setUser(user);

        applyRequest(experience, request);

        return toResponse(
                experienceRepository.save(experience)
        );
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getExperiences(Long userId) {
        return experienceRepository
                .findAllByUser_IdOrderByStartDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExperienceResponse getExperience(
            Long userId,
            Long experienceId
    ) {
        return toResponse(
                findOwnedExperience(userId, experienceId)
        );
    }

    @Transactional
    public ExperienceResponse updateExperience(
            Long userId,
            Long experienceId,
            ExperienceRequest request
    ) {
        validateDates(request);

        UserExperience experience =
                findOwnedExperience(userId, experienceId);

        applyRequest(experience, request);

        return toResponse(
                experienceRepository.save(experience)
        );
    }

    @Transactional
    public void deleteExperience(
            Long userId,
            Long experienceId
    ) {
        UserExperience experience =
                findOwnedExperience(userId, experienceId);

        experienceRepository.delete(experience);
    }

    private UserExperience findOwnedExperience(
            Long userId,
            Long experienceId
    ) {
        return experienceRepository
                .findByIdAndUser_Id(experienceId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Experience record not found"
                ));
    }

    private void validateDates(ExperienceRequest request) {

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getEndDate();

        if (
                startDate != null
                && endDate != null
                && endDate.isBefore(startDate)
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date"
            );
        }

        if (
                Boolean.TRUE.equals(request.getCurrentlyWorking())
                && endDate != null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must be empty when currently working"
            );
        }
    }

    private void applyRequest(
            UserExperience experience,
            ExperienceRequest request
    ) {
        experience.setCompanyName(
                request.getCompanyName().trim()
        );

        experience.setJobTitle(
                request.getJobTitle().trim()
        );

        experience.setEmploymentType(
                request.getEmploymentType()
        );

        experience.setLocation(
                clean(request.getLocation())
        );

        experience.setStartDate(
                request.getStartDate()
        );

        experience.setEndDate(
                request.getEndDate()
        );

        experience.setCurrentlyWorking(
                Boolean.TRUE.equals(request.getCurrentlyWorking())
        );

        experience.setDescription(
                clean(request.getDescription())
        );
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    private ExperienceResponse toResponse(
            UserExperience experience
    ) {
        return new ExperienceResponse(
                experience.getId(),
                experience.getCompanyName(),
                experience.getJobTitle(),
                experience.getEmploymentType(),
                experience.getLocation(),
                experience.getStartDate(),
                experience.getEndDate(),
                experience.isCurrentlyWorking(),
                experience.getDescription(),
                experience.getCreatedAt(),
                experience.getUpdatedAt()
        );
    }
}
