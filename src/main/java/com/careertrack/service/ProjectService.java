package com.careertrack.service;

import com.careertrack.dto.ProjectRequest;
import com.careertrack.dto.ProjectResponse;
import com.careertrack.entity.User;
import com.careertrack.entity.UserProject;
import com.careertrack.repository.UserProjectRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final UserProjectRepository projectRepository;

    public ProjectService(
            UserRepository userRepository,
            UserProjectRepository projectRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ProjectResponse createProject(
            Long userId,
            ProjectRequest request
    ) {
        validateDates(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        UserProject project = new UserProject();
        project.setUser(user);

        applyRequest(project, request);

        return toResponse(
                projectRepository.save(project)
        );
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjects(Long userId) {
        return projectRepository
                .findAllByUser_IdOrderByStartDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(
            Long userId,
            Long projectId
    ) {
        return toResponse(
                findOwnedProject(userId, projectId)
        );
    }

    @Transactional
    public ProjectResponse updateProject(
            Long userId,
            Long projectId,
            ProjectRequest request
    ) {
        validateDates(request);

        UserProject project =
                findOwnedProject(userId, projectId);

        applyRequest(project, request);

        return toResponse(
                projectRepository.save(project)
        );
    }

    @Transactional
    public void deleteProject(
            Long userId,
            Long projectId
    ) {
        UserProject project =
                findOwnedProject(userId, projectId);

        projectRepository.delete(project);
    }

    private UserProject findOwnedProject(
            Long userId,
            Long projectId
    ) {
        return projectRepository
                .findByIdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"
                ));
    }

    private void validateDates(ProjectRequest request) {

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
            UserProject project,
            ProjectRequest request
    ) {
        project.setProjectTitle(
                request.getProjectTitle().trim()
        );

        project.setDescription(
                clean(request.getDescription())
        );

        project.setTechStack(
                clean(request.getTechStack())
        );

        project.setGithubUrl(
                clean(request.getGithubUrl())
        );

        project.setLiveUrl(
                clean(request.getLiveUrl())
        );

        project.setStartDate(
                request.getStartDate()
        );

        project.setEndDate(
                request.getEndDate()
        );

        project.setCurrentlyWorking(
                Boolean.TRUE.equals(request.getCurrentlyWorking())
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

    private ProjectResponse toResponse(
            UserProject project
    ) {
        return new ProjectResponse(
                project.getId(),
                project.getProjectTitle(),
                project.getDescription(),
                project.getTechStack(),
                project.getGithubUrl(),
                project.getLiveUrl(),
                project.getStartDate(),
                project.getEndDate(),
                project.isCurrentlyWorking(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
