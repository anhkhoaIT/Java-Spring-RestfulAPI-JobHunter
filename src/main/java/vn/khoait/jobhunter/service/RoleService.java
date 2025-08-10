package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Permission;
import vn.khoait.jobhunter.domain.Role;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.repository.PermissionRepository;
import vn.khoait.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean isNameExist(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role fetchById(long id) {
        Optional<Role> op = this.roleRepository.findById(id);
        if(op.isPresent()) {
            return op.get();
        }
        return null;
    }

    public Role create(Role r) {
        //check permission
        if(r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions().stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(r);
    }

    public Role update(Role r) {
        Role roleDB = this.fetchById(r.getId());
        if(r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions().stream()
                .map(x -> x.getId())
                .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }   
            roleDB.setDescription(r.getDescription());
            roleDB.setName(r.getName());
            roleDB.setDescription(r.getDescription());
            roleDB.setActive(r.isActive());

            //update
            roleDB = this.roleRepository.save(roleDB);
            return roleDB;     
    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }

     public ResultPaginationDTO handleGetAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageRole.getNumber() + 1);
        mt.setPageSize(pageRole.getSize());
        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageRole.getContent());
        return rs;
    }
}
