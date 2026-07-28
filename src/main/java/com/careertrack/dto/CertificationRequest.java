package com.careertrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public class CertificationRequest {

    @NotBlank(message = "Certificate name is required")
    @Size(max = 200, message = "Certificate name cannot exceed 200 characters")
    private String certificateName;

    @NotBlank(message = "Issuing organization is required")
    @Size(max = 200, message = "Issuing organization cannot exceed 200 characters")
    private String issuingOrganization;

    private LocalDate issueDate;

    private LocalDate expirationDate;

    private Boolean doesNotExpire;

    @Size(max = 200, message = "Credential ID cannot exceed 200 characters")
    private String credentialId;

    @URL(message = "Credential URL must be valid")
    @Size(max = 500, message = "Credential URL cannot exceed 500 characters")
    private String credentialUrl;

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getDoesNotExpire() {
        return doesNotExpire;
    }

    public void setDoesNotExpire(Boolean doesNotExpire) {
        this.doesNotExpire = doesNotExpire;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }
}
