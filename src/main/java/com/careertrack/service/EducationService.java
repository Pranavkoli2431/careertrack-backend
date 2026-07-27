package com.careertrack.service;

import com.careertrack.dto.EducationRequest;
import com.careertrack.dto.EducationResponse;
import com.careertrack.entity.User;
import com.careertrack.entity.UserEducation;
import com.careertrack.repository.UserEducationRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class EducationService {

    private final UserRepository userRepository;
    private final UserEducationRepository educationRepository;

    public EducationService(
            UserRepository userRepository,
            UserEducationRepository educationRepository
    ) {
        this.userRepository = userRepository;
        this.educationRepository = educationRepository;
    }

    @Transactional
    public EducationResponse createEducation(
            Long userId,
            EducationRequest request
    ) {
        validateDates(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        UserEducation education = new UserEducation();
        education.setUser(user);

        applyRequest(education, request);

        return toResponse(
                educationRepository.save(education)
        );
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getEducations(Long userId) {
        return educationRepository
                .findAllByUser_IdOrderByStartDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EducationResponse getEducation(
            Long userId,
            Long educationId
    ) {
        return toResponse(
                findOwnedEducation(userId, educationId)
        );
    }

    @Transactional
    public EducationResponse updateEducation(
            Long userId,
            Long educationId,
            EducationRequest request
    ) {
        validateDates(request);

        UserEducation education =
                findOwnedEducation(userId, educationId);

        applyRequest(education, request);

        return toResponse(
                educationRepository.save(education)
        );
    }

    @Transactional
    public void deleteEducation(
            Long userId,
            Long educationId
    ) {
        UserEducation education =
                findOwnedEducation(userId, educationId);

        educationRepository.delete(education);
    }

    private UserEducation findOwnedEducation(
            Long userId,
            Long educationId
    ) {
        return educationRepository
                .findByIdAndUser_Id(educationId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Education record not found"
                ));
    }

    private void validateDates(EducationRequest request) {

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
                Boolean.TRUE.equals(request.getCurrentlyStudying())
                && endDate != null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must be empty when currently studying"
            );
        }
    }

    private void applyRequest(
            UserEducation education,
            EducationRequest request
    ) {
        education.setInstitutionName(
                request.getInstitutionName().trim()
        );

        education.setDegree(
                request.getDegree().trim()
        );

        education.setFieldOfStudy(
                clean(request.getFieldOfStudy())
        );

        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());

        education.setCurrentlyStudying(
                Boolean.TRUE.equals(request.getCurrentlyStudying())
        );

        education.setGrade(
                clean(request.getGrade())
        );

        education.setDescription(
                clean(request.getDescription())
        );
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty()
                ? null
                : trimmedValue;
    }

    private EducationResponse toResponse(
            UserEducation education
    ) {
        return new EducationResponse(
                education.getId(),
                education.getInstitutionName(),
                education.getDegree(),
                education.getFieldOfStudy(),
                education.getStartDate(),
                education.getEndDate(),
                education.isCurrentlyStudying(),
                education.getGrade(),
                education.getDescription(),
                education.getCreatedAt(),
                education.getUpdatedAt()
        );
    }
}
