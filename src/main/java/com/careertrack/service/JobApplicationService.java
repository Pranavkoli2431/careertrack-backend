package com.careertrack.service;

import com.careertrack.dto.JobApplicationRequest;
import com.careertrack.dto.JobApplicationResponse;
import com.careertrack.entity.ApplicationStatus;
import com.careertrack.entity.JobApplication;
import com.careertrack.entity.User;
import com.careertrack.repository.JobApplicationRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobApplicationService {

    private final UserRepository userRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(
            UserRepository userRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.userRepository = userRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional
    public JobApplicationResponse createApplication(
            Long userId,
            JobApplicationRequest request
    ) {
        validateDates(request.getAppliedDate(), request.getDeadline());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        JobApplication application = new JobApplication();
        application.setUser(user);

        applyRequest(application, request, true);

        return toResponse(
                jobApplicationRepository.save(application)
        );
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getApplications(
            Long userId,
            ApplicationStatus status
    ) {
        List<JobApplication> applications;

        if (status == null) {
            applications =
                    jobApplicationRepository
                            .findAllByUser_IdOrderByCreatedAtDesc(userId);
        } else {
            applications =
                    jobApplicationRepository
                            .findAllByUser_IdAndApplicationStatusOrderByCreatedAtDesc(
                                    userId,
                                    status
                            );
        }

        return applications.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getApplication(
            Long userId,
            Long applicationId
    ) {
        return toResponse(
                findOwnedApplication(userId, applicationId)
        );
    }

    @Transactional
    public JobApplicationResponse updateApplication(
            Long userId,
            Long applicationId,
            JobApplicationRequest request
    ) {
        validateDates(request.getAppliedDate(), request.getDeadline());

        JobApplication application =
                findOwnedApplication(userId, applicationId);

        applyRequest(application, request, false);

        return toResponse(
                jobApplicationRepository.save(application)
        );
    }

    @Transactional
    public void deleteApplication(
            Long userId,
            Long applicationId
    ) {
        JobApplication application =
                findOwnedApplication(userId, applicationId);

        jobApplicationRepository.delete(application);
    }

    private JobApplication findOwnedApplication(
            Long userId,
            Long applicationId
    ) {
        return jobApplicationRepository
                .findByIdAndUser_Id(applicationId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Job application not found"
                ));
    }

    private void applyRequest(
            JobApplication application,
            JobApplicationRequest request,
            boolean creating
    ) {
        application.setCompanyName(
                request.getCompanyName().trim()
        );

        application.setJobTitle(
                request.getJobTitle().trim()
        );

        application.setJobUrl(clean(request.getJobUrl()));
        application.setLocation(clean(request.getLocation()));
        application.setEmploymentType(request.getEmploymentType());
        application.setAppliedDate(request.getAppliedDate());
        application.setDeadline(request.getDeadline());
        application.setNotes(clean(request.getNotes()));

        if (request.getApplicationStatus() != null) {
            application.setApplicationStatus(
                    request.getApplicationStatus()
            );
        } else if (creating) {
            application.setApplicationStatus(
                    ApplicationStatus.APPLIED
            );
        }
    }

    private void validateDates(
            LocalDate appliedDate,
            LocalDate deadline
    ) {
        if (
                appliedDate != null
                && deadline != null
                && deadline.isBefore(appliedDate)
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Deadline cannot be before applied date"
            );
        }
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    private JobApplicationResponse toResponse(
            JobApplication application
    ) {
        return new JobApplicationResponse(
                application.getId(),
                application.getCompanyName(),
                application.getJobTitle(),
                application.getJobUrl(),
                application.getLocation(),
                application.getEmploymentType(),
                application.getApplicationStatus(),
                application.getAppliedDate(),
                application.getDeadline(),
                application.getNotes(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
