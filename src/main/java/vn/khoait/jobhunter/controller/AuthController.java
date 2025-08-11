package vn.khoait.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.request.ReqLoginDTO;
import vn.khoait.jobhunter.domain.response.ResLoginDTO;
import vn.khoait.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.khoait.jobhunter.service.UserService;
import vn.khoait.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${khoait.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService
    ,PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        //Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken 
        = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Create token
        
        ResLoginDTO result = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(loginDto.getUsername());
        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(), currentUserDB.getName(), currentUserDB.getRole());
            result.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), result);
        result.setAccessToken(access_token);
        //create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDto.getUsername(), result);
        this.userService.updateUserToken(refresh_token, loginDto.getUsername());

        //create cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();
        return ResponseEntity
        .ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(result);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User newUser) throws BadCredentialsException{
        boolean isEmailExist = this.userService.isEmailExist(newUser.getEmail());
        if(isEmailExist) {
            throw new BadCredentialsException("Email " + newUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(newUser);
        ResCreateUserDTO resCreateUserDTO = this.userService.convertToResCreateUserDTO(createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);

    }

    @GetMapping("/auth/account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
        SecurityUtil.getCurrentUserLogin().get() : "";

        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userLGetAccount = new ResLoginDTO.UserGetAccount();
        if(currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setName(currentUserDB.getName());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setRole(currentUserDB.getRole());
            userLGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userLGetAccount);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<ResLoginDTO> getRefreshToken(@CookieValue(name = "refresh_token") String refresh_token) throws BadCredentialsException{
        //Check token xem co valid hay khong
        Jwt checkValidRefreshToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = checkValidRefreshToken.getSubject();
        //Check 2: trong DB co token va email hay không
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if(currentUser == null) {
            throw new BadCredentialsException("Refresh Token không hợp lệ");
        }
        //ddddddddd
        ResLoginDTO result = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if(currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),currentUserDB.getEmail(), currentUserDB.getName(), currentUser.getRole());
            result.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(email, result);
        result.setAccessToken(access_token);
        //create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, result);
        this.userService.updateUserToken(refresh_token, email);

        //create cookie
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", new_refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();
        return ResponseEntity
        .ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(result);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() throws BadCredentialsException{
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ?
                        SecurityUtil.getCurrentUserLogin().get() : "";
        if(email.equals("")) {
            throw new BadCredentialsException("Access Token không hợp lệ");
        }
        //Update refresh token = null
        this.userService.updateUserToken(null, email);
        //Remove refresh token cookie

        ResponseCookie deleteSpringCookie = ResponseCookie.from("refresh_token", null)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();
        return ResponseEntity
        .ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).body(null);
    }
}
