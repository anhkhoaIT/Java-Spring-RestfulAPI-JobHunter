package vn.khoait.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Company;
import vn.khoait.jobhunter.domain.Skill;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill findByName(String name) {
        return this.skillRepository.findByName(name);
    }

   

    public Skill handleCreateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill handleUpdateSkill(Skill skill) {
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        if (skillOptional.isPresent()) {
            Skill currentSkill = skillOptional.get();
            currentSkill.setId(skill.getId());
            currentSkill.setName((skill.getName()));
            return this.skillRepository.save(currentSkill);
        }
        return null;
    }

    public ResultPaginationDTO handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageSkill.getNumber() + 1);
        mt.setPageSize(pageSkill.getSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }
}
