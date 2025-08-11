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
import vn.khoait.jobhunter.domain.Permission;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.service.PermissionService;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    @PostMapping("/permissions") 
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission p) throws BadCredentialsException{
        //check exist
        if(this.permissionService.isPermissionExist(p)) {
            throw new BadCredentialsException("Permission đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(p));
    }

    @PutMapping("/permissions") 
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission p) throws BadCredentialsException{
        //check exist by id
        if(this.permissionService.fetchById(p.getId()) == null) {
            throw new BadCredentialsException("Permission với id" + p.getId() + " không tồn tại");  
        }
        //check exist permission
        if(this.permissionService.isPermissionExist(p)) {
                throw new BadCredentialsException("Permission đã tồn tại");
        }
        return ResponseEntity.ok().body(this.permissionService.update(p));
    }

    @DeleteMapping("/permissions/{id}") 
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws BadCredentialsException{
        //check exist by id
        if(this.permissionService.fetchById(id) == null) {
            throw new BadCredentialsException("Permission với id" + id + " đã tồn tại");  
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions") 
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec, Pageable pageable) throws BadCredentialsException{
        

        return ResponseEntity.ok().body(this.permissionService.handleGetAllPermissions(spec, pageable));
    }
}
