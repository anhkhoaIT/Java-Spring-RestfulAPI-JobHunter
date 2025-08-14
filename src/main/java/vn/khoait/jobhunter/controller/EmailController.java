package vn.khoait.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Email;
import vn.khoait.jobhunter.domain.RestResponse;
import vn.khoait.jobhunter.service.EmailService;
import vn.khoait.jobhunter.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;
    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }
    
    @GetMapping("/email")
    public ResponseEntity<RestResponse> sendSimpleEmail() {
        // this.emailService.sendSimpleEmail();
        // this.emailService.sendEmailSync("super.khoa14@gmail.com", "test send email", "<h1><b> HELLO <b></h1>", false, true);
        // this.emailService.sendEmailFromTemplateSync("super.khoa14@gmail.com", "test send email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(200);
        restResponse.setData("Success");

        // Bây giờ, kiểu trả về đã khớp, nên không còn lỗi nữa.
        return ResponseEntity.ok(restResponse); 
    }
}
