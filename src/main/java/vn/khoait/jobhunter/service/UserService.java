package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }


    public void handleDeleteUser(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            this.userRepository.delete(user);
        }
    }

    public User fetchUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            return user;
        }
        return null;
    }

    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    public User updateUser(User modifiedUser) {
        Optional<User> userOptional = this.userRepository.findById(modifiedUser.getId());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(modifiedUser.getEmail());
            user.setName(modifiedUser.getName());
            user.setPassword(modifiedUser.getPassword());
            return this.userRepository.save(user);
        }
        return null;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
