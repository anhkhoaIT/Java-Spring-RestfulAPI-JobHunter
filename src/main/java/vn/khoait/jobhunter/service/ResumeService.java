package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Job;
import vn.khoait.jobhunter.domain.Resume;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.khoait.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.khoait.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.khoait.jobhunter.repository.ResumeRepository;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Optional<Resume> fetchResumeById(long id) {
        return this.resumeRepository.findById(id);
    }

    public ResCreateResumeDTO handleCreateResume(Resume resume) {
        Resume currentResume = this.resumeRepository.save(resume);
        ResCreateResumeDTO res = new ResCreateResumeDTO();
        res.setId(currentResume.getId());
        res.setCreatedAt(currentResume.getCreatedAt());
        res.setCreatedBy(currentResume.getCreatedBy());
        return res;
    }

    public ResUpdateResumeDTO handleUpdateResume(Resume resume) {
        Resume updateResume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO res = new ResUpdateResumeDTO();
        res.setUpdatedAt(updateResume.getUpdatedAt());
        res.setUpdatedBy(updateResume.getUpdatedBy());
        return res;
    }

    public void handleDeleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume) {

        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setCreatedBy(resume.getCreatedBy());
        res.setUpdatedBy(resume.getUpdatedBy());

        if(resume.getJob() != null) {
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return res;
    }

     public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageResume.getNumber() + 1);
        mt.setPageSize(pageResume.getSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        //Remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent().stream()
            .map(item -> this.getResume(item))
            .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
        
    }

    
}
