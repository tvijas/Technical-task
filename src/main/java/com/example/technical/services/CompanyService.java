package com.example.technical.services;

import com.example.technical.exceptions.NotFoundException;
import com.example.technical.models.dto.CompanyDTO;
import com.example.technical.models.entity.Company;
import com.example.technical.repos.CompanyRepo;
import com.example.technical.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final Mapper mapper;
    private final CompanyRepo companyRepo;

    @Transactional
    public CompanyDTO create(Company company) {
        return mapper.convertCompanyToDTO(companyRepo.save(company));
    }

    public CompanyDTO getById(UUID id) {
        return mapper.convertCompanyToDTO(companyRepo.findById(id).orElseThrow(() ->
                new NotFoundException(Map.of("id", "Company with such id not found"))));
    }

    public List<CompanyDTO> getAll() {
        return companyRepo.findAll().stream()
                .map(mapper::convertCompanyToDTO)
                .toList();
    }

    @Transactional
    public CompanyDTO update(Company company) {
        if (!companyRepo.existsById(company.getId()))
            throw new NotFoundException(Map.of("id", "Company with such id not found"));
        return mapper.convertCompanyToDTO(companyRepo.save(company));
    }
    @Transactional
    public void delete(UUID id) {
        if (!companyRepo.existsById(id))
            throw new NotFoundException(Map.of("id", "Company with such id not found"));
        companyRepo.deleteById(id);
    }
}
