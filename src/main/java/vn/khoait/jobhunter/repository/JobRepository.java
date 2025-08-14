package vn.khoait.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.khoait.jobhunter.domain.Job;
import vn.khoait.jobhunter.domain.Skill;


@Repository
public interface JobRepository extends JpaRepository<Job,Long>, JpaSpecificationExecutor<Job>{
    List<Job> findBySkillsIn(List<Skill> skills);
    
} 
