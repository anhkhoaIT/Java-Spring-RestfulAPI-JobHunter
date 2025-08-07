package vn.khoait.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.dto.LoginDTO;
import vn.khoait.jobhunter.domain.dto.ResLoginDTO;
import vn.khoait.jobhunter.service.UserService;
import vn.khoait.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken 
        = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Create token
        String access_token = this.securityUtil.createToken(authentication);
        ResLoginDTO result = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(), currentUserDB.getName());
            result.setUser(userLogin);
        }
        
        result.setAccessToken(access_token);
        return ResponseEntity.ok().body(result);
    }
}
