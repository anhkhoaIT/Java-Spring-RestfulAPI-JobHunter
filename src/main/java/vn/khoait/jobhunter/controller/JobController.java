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
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.khoait.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.khoait.jobhunter.service.JobService;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs") 
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.handleCreateJob(job));
    }

    @PutMapping("/jobs") 
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws BadCredentialsException{
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if(!currentJob.isPresent()) {
            throw new BadCredentialsException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.handleUpdateJob(job, currentJob.get()));
    }

    @DeleteMapping("/jobs/{id}") 
    public ResponseEntity<Void> updateJob(@PathVariable("id") long id) throws BadCredentialsException{
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if(!currentJob.isPresent()) {
            throw new BadCredentialsException("Job not found");
        }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}") 
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws BadCredentialsException{
        Optional<Job> jobOptional = this.jobService.fetchJobById(id);
        if(!jobOptional.isPresent()) {
            throw new BadCredentialsException("Job not found");
        }
        return ResponseEntity.ok().body(jobOptional.get());
    }

    @GetMapping("/jobs") 
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetAllJobs(spec,pageable));
    }
}
    
