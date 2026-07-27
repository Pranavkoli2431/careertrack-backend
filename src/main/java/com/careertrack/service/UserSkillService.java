package com.careertrack.service;

import com.careertrack.dto.SkillRequest;
import com.careertrack.dto.SkillResponse;
import com.careertrack.entity.User;
import com.careertrack.entity.UserSkill;
import com.careertrack.repository.UserRepository;
import com.careertrack.repository.UserSkillRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserSkillService {

    private final UserRepository userRepository;
    private final UserSkillRepository userSkillRepository;

    public UserSkillService(
            UserRepository userRepository,
            UserSkillRepository userSkillRepository
    ) {
        this.userRepository = userRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @Transactional
    public SkillResponse addSkill(
            Long userId,
            SkillRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        String skillName = request.getSkillName().trim();

        if (userSkillRepository
                .existsByUser_IdAndSkillNameIgnoreCase(
                        userId,
                        skillName
                )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This skill has already been added"
            );
        }

        UserSkill skill = new UserSkill();
        skill.setUser(user);
        skill.setSkillName(skillName);
        skill.setProficiency(request.getProficiency());
        skill.setYearsOfExperience(
                request.getYearsOfExperience()
        );

        return toResponse(userSkillRepository.save(skill));
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getSkills(Long userId) {

        return userSkillRepository
                .findAllByUser_IdOrderBySkillNameAsc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SkillResponse updateSkill(
            Long userId,
            Long skillId,
            SkillRequest request
    ) {
        UserSkill skill = findOwnedSkill(userId, skillId);

        String skillName = request.getSkillName().trim();

        boolean duplicateExists = userSkillRepository
                .existsByUser_IdAndSkillNameIgnoreCaseAndIdNot(
                        userId,
                        skillName,
                        skillId
                );

        if (duplicateExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This skill has already been added"
            );
        }

        skill.setSkillName(skillName);
        skill.setProficiency(request.getProficiency());
        skill.setYearsOfExperience(
                request.getYearsOfExperience()
        );

        return toResponse(userSkillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(
            Long userId,
            Long skillId
    ) {
        UserSkill skill = findOwnedSkill(userId, skillId);
        userSkillRepository.delete(skill);
    }

    private UserSkill findOwnedSkill(
            Long userId,
            Long skillId
    ) {
        return userSkillRepository
                .findByIdAndUser_Id(skillId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Skill not found"
                ));
    }

    private SkillResponse toResponse(UserSkill skill) {

        return new SkillResponse(
                skill.getId(),
                skill.getSkillName(),
                skill.getProficiency(),
                skill.getYearsOfExperience(),
                skill.getCreatedAt(),
                skill.getUpdatedAt()
        );
    }
}
