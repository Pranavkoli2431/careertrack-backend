package com.careertrack.repository;

import com.careertrack.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProjectRepository
        extends JpaRepository<UserProject, Long> {

    List<UserProject> findAllByUser_IdOrderByStartDateDesc(
            Long userId
    );

    Optional<UserProject> findByIdAndUser_Id(
            Long projectId,
            Long userId
    );
}
