package vn.khoait.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.khoait.jobhunter.domain.Company;
import vn.khoait.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company newCompany) {
        return this.companyRepository.save(newCompany);
    }

    public List<Company> handleGetAllCompanies() {
        return this.companyRepository.findAll();
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
        this.companyRepository.deleteById(id);
    
    }
}
