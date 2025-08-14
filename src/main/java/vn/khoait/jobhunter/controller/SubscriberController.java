package vn.khoait.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import vn.khoait.jobhunter.domain.Subscriber;
import vn.khoait.jobhunter.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;
    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers") 
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber sub) throws BadCredentialsException{
        boolean isExist = this.subscriberService.isExistsByEmail(sub.getEmail());
        if(isExist == true) {
            throw new BadCredentialsException("Email = " + sub.getEmail() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(sub));
    }

    @PutMapping("/subscribers") 
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subReq) throws BadCredentialsException{
        //check id
        Subscriber subsDB = this.subscriberService.findById(subReq.getId());
        if(subsDB == null) {
            throw new BadCredentialsException("Id " + subReq.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.subscriberService.update(subsDB, subReq));
    }
}
