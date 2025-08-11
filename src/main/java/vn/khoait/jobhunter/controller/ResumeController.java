package vn.khoait.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.khoait.jobhunter.domain.Job;
import vn.khoait.jobhunter.domain.Resume;
import vn.khoait.jobhunter.domain.Skill;
import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.khoait.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.khoait.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.khoait.jobhunter.service.JobService;
import vn.khoait.jobhunter.service.ResumeService;
import vn.khoait.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/v1") 
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final JobService jobService;
    public ResumeController(ResumeService resumeService,UserService userService,
                                    JobService jobService) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
    }

    @PostMapping("/resumes") 
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws BadCredentialsException{
        User currentUser = this.userService.fetchUserById(resume.getUser().getId());
        Optional<Job> jobOptional = this.jobService.fetchJobById(resume.getJob().getId());
        if(currentUser == null) {
            throw new BadCredentialsException("User với id = " + resume.getUser().getId() + " không tồn tại");
        }

        if(!jobOptional.isPresent()) {
            throw new BadCredentialsException("Job với id = " + resume.getJob().getId() + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));
    }

    @PutMapping("/resumes") 
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws BadCredentialsException{
        Optional<Resume> reqResumeOptional = this.resumeService.fetchResumeById(resume.getId());
        if(reqResumeOptional.isEmpty()) {
            throw new BadCredentialsException("Resume với id = " + resume.getId() + " không tồn tại");
        }
        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(reqResume));
    }

    @DeleteMapping("/resumes/{id}") 
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws BadCredentialsException{
        Optional<Resume> currentResume = this.resumeService.fetchResumeById(id);
        if(!currentResume.isPresent()) {
            throw new BadCredentialsException("Resume not found");
        }
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}") 
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id) throws BadCredentialsException{
        Optional<Resume> reqResumeOptional = this.resumeService.fetchResumeById(id);
        if(reqResumeOptional.isEmpty()) {
            throw new BadCredentialsException("Resume với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.resumeService.getResume(reqResumeOptional.get()));
    }

    @GetMapping("/resumes") 
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Resume> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec,pageable));
    }

    @PostMapping("/resumes/by-user") 
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
