package vn.khoait.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
import vn.khoait.jobhunter.domain.Job;
import vn.khoait.jobhunter.domain.Permission;
import vn.khoait.jobhunter.domain.Role;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.khoait.jobhunter.service.JobService;
import vn.khoait.jobhunter.service.RoleService;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @PostMapping("/roles") 
    public ResponseEntity<Role> create(@Valid @RequestBody Role r) throws BadCredentialsException{
        //check name
        if(this.roleService.isNameExist(r.getName())) {
            throw new BadCredentialsException("Role với name = " + r.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(r));
    }

    @PutMapping("/roles") 
    public ResponseEntity<Role> update(@Valid @RequestBody Role r) throws BadCredentialsException{
        //check exist by id
        if(this.roleService.fetchById(r.getId()) == null) {
            throw new BadCredentialsException("Role với id" + r.getId() + " không tồn tại");  
        }
        //check name
        if(this.roleService.isNameExist(r.getName())) {
            throw new BadCredentialsException("Role với name = " + r.getName() + " đã tồn tại");
        }
        return ResponseEntity.ok().body(this.roleService.update(r));
    }

    @DeleteMapping("/roles/{id}") 
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws BadCredentialsException{
        //check exist by id
        if(this.roleService.fetchById(id) == null) {
            throw new BadCredentialsException("Role với id" + id + " không tồn tại");  
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles") 
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) throws BadCredentialsException{
        
        return ResponseEntity.ok().body(this.roleService.handleGetAllRoles(spec, pageable));
    }
}
