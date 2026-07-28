package com.careertrack.service;

import com.careertrack.dto.CertificationRequest;
import com.careertrack.dto.CertificationResponse;
import com.careertrack.entity.User;
import com.careertrack.entity.UserCertification;
import com.careertrack.repository.UserCertificationRepository;
import com.careertrack.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CertificationService {

    private final UserRepository userRepository;
    private final UserCertificationRepository certificationRepository;

    public CertificationService(
            UserRepository userRepository,
            UserCertificationRepository certificationRepository
    ) {
        this.userRepository = userRepository;
        this.certificationRepository = certificationRepository;
    }

    @Transactional
    public CertificationResponse createCertification(
            Long userId,
            CertificationRequest request
    ) {
        validateDates(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        UserCertification certification = new UserCertification();
        certification.setUser(user);

        applyRequest(certification, request);

        return toResponse(
                certificationRepository.save(certification)
        );
    }

    @Transactional(readOnly = true)
    public List<CertificationResponse> getCertifications(Long userId) {
        return certificationRepository
                .findAllByUser_IdOrderByIssueDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CertificationResponse getCertification(
            Long userId,
            Long certificationId
    ) {
        return toResponse(
                findOwnedCertification(userId, certificationId)
        );
    }

    @Transactional
    public CertificationResponse updateCertification(
            Long userId,
            Long certificationId,
            CertificationRequest request
    ) {
        validateDates(request);

        UserCertification certification =
                findOwnedCertification(userId, certificationId);

        applyRequest(certification, request);

        return toResponse(
                certificationRepository.save(certification)
        );
    }

    @Transactional
    public void deleteCertification(
            Long userId,
            Long certificationId
    ) {
        UserCertification certification =
                findOwnedCertification(userId, certificationId);

        certificationRepository.delete(certification);
    }

    private UserCertification findOwnedCertification(
            Long userId,
            Long certificationId
    ) {
        return certificationRepository
                .findByIdAndUser_Id(certificationId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Certification not found"
                ));
    }

    private void validateDates(CertificationRequest request) {

        LocalDate issueDate = request.getIssueDate();
        LocalDate expirationDate = request.getExpirationDate();

        if (
                issueDate != null
                && expirationDate != null
                && expirationDate.isBefore(issueDate)
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expiration date cannot be before issue date"
            );
        }

        if (
                Boolean.TRUE.equals(request.getDoesNotExpire())
                && expirationDate != null
        ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expiration date must be empty when certificate does not expire"
            );
        }
    }

    private void applyRequest(
            UserCertification certification,
            CertificationRequest request
    ) {
        certification.setCertificateName(
                request.getCertificateName().trim()
        );

        certification.setIssuingOrganization(
                request.getIssuingOrganization().trim()
        );

        certification.setIssueDate(request.getIssueDate());
        certification.setExpirationDate(request.getExpirationDate());

        certification.setDoesNotExpire(
                Boolean.TRUE.equals(request.getDoesNotExpire())
        );

        certification.setCredentialId(
                clean(request.getCredentialId())
        );

        certification.setCredentialUrl(
                clean(request.getCredentialUrl())
        );
    }

    private String clean(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();

        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    private CertificationResponse toResponse(
            UserCertification certification
    ) {
        return new CertificationResponse(
                certification.getId(),
                certification.getCertificateName(),
                certification.getIssuingOrganization(),
                certification.getIssueDate(),
                certification.getExpirationDate(),
                certification.isDoesNotExpire(),
                certification.getCredentialId(),
                certification.getCredentialUrl(),
                certification.getCreatedAt(),
                certification.getUpdatedAt()
        );
    }
}
