package com.careertrack.repository;

import com.careertrack.entity.UserEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserEducationRepository
        extends JpaRepository<UserEducation, Long> {

    List<UserEducation> findAllByUser_IdOrderByStartDateDesc(
            Long userId
    );

    Optional<UserEducation> findByIdAndUser_Id(
            Long educationId,
            Long userId
    );
}
