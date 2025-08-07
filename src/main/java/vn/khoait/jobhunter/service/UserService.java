package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.dto.Meta;
import vn.khoait.jobhunter.domain.dto.ResCreateUserDTO;
import vn.khoait.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.khoait.jobhunter.domain.dto.ResUserDTO;
import vn.khoait.jobhunter.domain.dto.ResultPaginationDTO;
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

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    public ResUpdateUserDTO convertoResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setGender(user.getGender());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
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

    public ResultPaginationDTO fetchAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
        .stream().map(item -> new ResUserDTO(
            item.getId(),
            item.getName(),
            item.getEmail(),
            item.getAge(),
            item.getGender(),
            item.getAddress(),
            item.getCreatedAt(),
            item.getUpdatedAt()
        )).collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User updateUser(User modifiedUser) {
        Optional<User> userOptional = this.userRepository.findById(modifiedUser.getId());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAddress(modifiedUser.getAddress());
            user.setName(modifiedUser.getName());
            user.setGender(modifiedUser.getGender());
            user.setAge(modifiedUser.getAge());
            return this.userRepository.save(user);
        }
        return null;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
