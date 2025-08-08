package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Company;
import vn.khoait.jobhunter.domain.User;
import vn.khoait.jobhunter.domain.response.ResultPaginationDTO;
import vn.khoait.jobhunter.repository.CompanyRepository;
import vn.khoait.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository){
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company newCompany) {
        return this.companyRepository.save(newCompany);
    }

    public ResultPaginationDTO handleGetAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setAddress(company.getAddress());
            currentCompany.setName(company.getName());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setLogo(company.getLogo());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }
    
    public void handleDeleteCompany(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if(companyOptional.isPresent()) {
            Company com = companyOptional.get();
            List<User> listUser = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(listUser);
        }
        this.companyRepository.deleteById(id);
    
    }
    
    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }
}
