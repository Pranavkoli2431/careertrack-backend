package com.careertrack.dto;

import com.careertrack.entity.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ExperienceRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name cannot exceed 200 characters")
    private String companyName;

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title cannot exceed 150 characters")
    private String jobTitle;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @Size(max = 150, message = "Location cannot exceed 150 characters")
    private String location;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "Currently working value is required")
    private Boolean currentlyWorking;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
