package vn.khoait.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.repository.UserRepository;
import vn.khoait.jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User newUser) {
        String hashPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {

        this.userService.handleDeleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("delete user successfully");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> GetUser(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> GetAllUsers() {
        List<User> listUser = this.userService.fetchAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(listUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> UpdateUser(@RequestBody User userBody) {
        User result = this.userService.updateUser(userBody);
         return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
