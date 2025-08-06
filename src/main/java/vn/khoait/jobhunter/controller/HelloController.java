package vn.khoait.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.khoait.jobhunter.domain.RestResponse;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<RestResponse> getHelloWorld() {
       RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(200);
        restResponse.setData("Success");

        // Bây giờ, kiểu trả về đã khớp, nên không còn lỗi nữa.
        return ResponseEntity.ok(restResponse); 
    }
}
