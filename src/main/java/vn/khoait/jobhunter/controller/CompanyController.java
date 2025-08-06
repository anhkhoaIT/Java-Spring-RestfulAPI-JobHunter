package vn.khoait.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.khoait.jobhunter.domain.Company;
import vn.khoait.jobhunter.service.CompanyService;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company newCompany) {
        Company company = this.companyService.handleCreateCompany(newCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }
    
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> listCompany = this.companyService.handleGetAllCompanies();
        return ResponseEntity.ok(listCompany);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        Company updatedcCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body(updatedcCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long companyId) {
        this.companyService.handleDeleteCompany(companyId);
        return ResponseEntity.ok(null);
    }
}
