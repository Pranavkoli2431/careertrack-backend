package com.careertrack.repository;

import com.careertrack.entity.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCertificationRepository
        extends JpaRepository<UserCertification, Long> {

    List<UserCertification> findAllByUser_IdOrderByIssueDateDesc(
            Long userId
    );

    Optional<UserCertification> findByIdAndUser_Id(
            Long certificationId,
            Long userId
    );
}
