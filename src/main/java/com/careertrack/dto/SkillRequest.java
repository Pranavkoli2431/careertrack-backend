package com.careertrack.dto;

import com.careertrack.entity.SkillProficiency;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100, message = "Skill name cannot exceed 100 characters")
    private String skillName;

    @NotNull(message = "Proficiency is required")
    private SkillProficiency proficiency;

    @DecimalMin(
            value = "0.0",
            message = "Years of experience cannot be negative"
    )
    @DecimalMax(
            value = "80.0",
            message = "Years of experience cannot exceed 80"
    )
    @Digits(
            integer = 2,
            fraction = 1,
            message = "Years of experience can contain only one decimal place"
    )
    private BigDecimal yearsOfExperience;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public SkillProficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(SkillProficiency proficiency) {
        this.proficiency = proficiency;
    }

    public BigDecimal getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(BigDecimal yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
}
