package vn.khoait.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import vn.khoait.jobhunter.domain.Skill;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.service.SkillService;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    @PostMapping("/skills") 
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws BadCredentialsException{
        Skill currSkill = this.skillService.findByName(skill.getName());
        if(currSkill != null) {
            throw new BadCredentialsException("Skill = " + skill.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills") 
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill){
        Skill result = this.skillService.handleUpdateSkill(skill);
        if(result == null) {
            throw new BadCredentialsException("Id skill không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetAllSkills(spec,pageable));
    }
}
