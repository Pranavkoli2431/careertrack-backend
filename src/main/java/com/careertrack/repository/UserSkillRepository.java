package com.careertrack.repository;

import com.careertrack.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository
        extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findAllByUser_IdOrderBySkillNameAsc(
            Long userId
    );

    Optional<UserSkill> findByIdAndUser_Id(
            Long skillId,
            Long userId
    );

    boolean existsByUser_IdAndSkillNameIgnoreCase(
            Long userId,
            String skillName
    );

    boolean existsByUser_IdAndSkillNameIgnoreCaseAndIdNot(
            Long userId,
            String skillName,
            Long skillId
    );
}
