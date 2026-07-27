package com.careertrack.service;

import com.careertrack.dto.DashboardSummaryResponse;
import com.careertrack.entity.ApplicationStatus;
import com.careertrack.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final JobApplicationRepository jobApplicationRepository;

    public DashboardService(
            JobApplicationRepository jobApplicationRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary(Long userId) {

        long totalApplications =
                jobApplicationRepository.countByUser_Id(userId);

        long wishlist = countByStatus(
                userId,
                ApplicationStatus.WISHLIST
        );

        long applied = countByStatus(
                userId,
                ApplicationStatus.APPLIED
        );

        long assessment = countByStatus(
                userId,
                ApplicationStatus.ASSESSMENT
        );

        long interview = countByStatus(
                userId,
                ApplicationStatus.INTERVIEW
        );

        long offered = countByStatus(
                userId,
                ApplicationStatus.OFFERED
        );

        long rejected = countByStatus(
                userId,
                ApplicationStatus.REJECTED
        );

        long withdrawn = countByStatus(
                userId,
                ApplicationStatus.WITHDRAWN
        );

        return new DashboardSummaryResponse(
                totalApplications,
                wishlist,
                applied,
                assessment,
                interview,
                offered,
                rejected,
                withdrawn
        );
    }

    private long countByStatus(
            Long userId,
            ApplicationStatus status
    ) {
        return jobApplicationRepository
                .countByUser_IdAndApplicationStatus(
                        userId,
                        status
                );
    }
}
