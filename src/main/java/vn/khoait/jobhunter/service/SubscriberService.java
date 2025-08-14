package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Job;
import vn.khoait.jobhunter.domain.Skill;
import vn.khoait.jobhunter.domain.Subscriber;
import vn.khoait.jobhunter.repository.JobRepository;
import vn.khoait.jobhunter.repository.SkillRepository;
import vn.khoait.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;
    public SubscriberService(SubscriberRepository subscriberRepository,SkillRepository skillRepository,JobRepository jobRepository,EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean isExistsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }
    // @Scheduled(fixedRate = 5000)

    // public void cron() {
    //     System.out.println(">>>> HIHI");
    // }

    public Subscriber findById(long id) {
        Optional<Subscriber> op = this.subscriberRepository.findById(id);
        return op.isPresent() ? op.get() : null;
    }

    public Subscriber create(Subscriber sub) {
        if(sub.getSkills() != null) {
            List<Long> skillId = sub.getSkills().stream()
                                .map(x -> x.getId())
                                .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(skillId);
            sub.setSkills(dbSkill);
        }

        return this.subscriberRepository.save(sub);
    }

    public Subscriber update(Subscriber subDB, Subscriber supReq) {
        if(supReq.getSkills() != null) {
            List<Long> skillId = supReq.getSkills().stream()
                                .map(x -> x.getId())
                                .collect(Collectors.toList());
            List<Skill> dbSkill = this.skillRepository.findByIdIn(skillId);
            subDB.setSkills(dbSkill);
        }
        return this.subscriberRepository.save(subDB);
    }

     public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        // List<ResEmailJob> arr = listJobs.stream().map(
                        // job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }

 }
