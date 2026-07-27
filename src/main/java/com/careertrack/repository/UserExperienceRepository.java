package com.careertrack.repository;

import com.careertrack.entity.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserExperienceRepository
        extends JpaRepository<UserExperience, Long> {

    List<UserExperience> findAllByUser_IdOrderByStartDateDesc(
            Long userId
    );

    Optional<UserExperience> findByIdAndUser_Id(
            Long experienceId,
            Long userId
    );
}
