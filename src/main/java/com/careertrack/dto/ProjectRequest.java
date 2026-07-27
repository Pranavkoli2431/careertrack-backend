package com.careertrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class ProjectRequest {

    @NotBlank(message = "Project title is required")
    @Size(
            max = 200,
            message = "Project title cannot exceed 200 characters"
    )
    private String projectTitle;

    @Size(
            max = 3000,
            message = "Description cannot exceed 3000 characters"
    )
    private String description;

    @Size(
            max = 1000,
            message = "Technology stack cannot exceed 1000 characters"
    )
    private String techStack;

    @URL(message = "GitHub URL must be valid")
    @Size(
            max = 500,
            message = "GitHub URL cannot exceed 500 characters"
    )
    private String githubUrl;

    @URL(message = "Live URL must be valid")
    @Size(
            max = 500,
            message = "Live URL cannot exceed 500 characters"
    )
    private String liveUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean currentlyWorking;

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(Boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }
}
