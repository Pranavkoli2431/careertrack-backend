package com.careertrack.repository;

import com.careertrack.entity.ApplicationStatus;
import com.careertrack.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findAllByUser_IdOrderByCreatedAtDesc(
            Long userId
    );

    List<JobApplication>
    findAllByUser_IdAndApplicationStatusOrderByCreatedAtDesc(
            Long userId,
            ApplicationStatus applicationStatus
    );

    Optional<JobApplication> findByIdAndUser_Id(
            Long applicationId,
            Long userId
    );

    long countByUser_Id(Long userId);

    long countByUser_IdAndApplicationStatus(
            Long userId,
            ApplicationStatus applicationStatus
    );
}
