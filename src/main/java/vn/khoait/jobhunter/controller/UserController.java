package vn.khoait.jobhunter.controller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.dto.ResCreateUserDTO;
import vn.khoait.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.khoait.jobhunter.domain.dto.ResUserDTO;
import vn.khoait.jobhunter.domain.dto.ResultPaginationDTO;
import vn.khoait.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User newUser) throws BadCredentialsException{
        boolean isEmailExist = this.userService.isEmailExist(newUser.getEmail());
        if(isEmailExist) {
            throw new BadCredentialsException("Email " + newUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashPassword);
        User createUser = this.userService.handleCreateUser(newUser);
        ResCreateUserDTO resCreateUserDTO = this.userService.convertToResCreateUserDTO(createUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUserDTO);

    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long userId) throws BadCredentialsException{
        User currentUser = this.userService.fetchUserById(userId);
        if(currentUser == null) {
            throw new BadCredentialsException("User với id = " + userId + " không tồn tại");
        }
        this.userService.handleDeleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResUserDTO> GetUser(@PathVariable("id") long id) throws BadCredentialsException{
        User user = this.userService.fetchUserById(id);
        if(user == null) {
            throw new BadCredentialsException("User với id = " + id + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(user));
    }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> GetAllUsers(@Filter Specification<User> spec, Pageable pageable) 
        {
            // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
            // String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
            // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));
            
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(spec, pageable));
        }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> UpdateUser(@RequestBody User userBody) throws BadCredentialsException{
        User result = this.userService.updateUser(userBody);
        if(result == null) {
            throw new BadCredentialsException("User với id = " + userBody.getId() + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertoResUpdateUserDTO(result));
    }
}
