package com.example.technical.utils;

import com.example.technical.models.dto.CompanyDTO;
import com.example.technical.models.entity.Company;
import com.example.technical.models.request.CreateCompanyRequest;
import com.example.technical.models.request.UpdateCompanyRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;

    public CompanyDTO convertCompanyToDTO(Company company) {
        return modelMapper.map(company, CompanyDTO.class);
    }

    public Company convertUpdateCompanyRequestToCompany(UpdateCompanyRequest updateCompanyRequest) {
        return modelMapper.map(updateCompanyRequest, Company.class);
    }

    public Company convertCreateCompanyRequestToCompany(CreateCompanyRequest createCompanyRequest) {
        return modelMapper.map(createCompanyRequest, Company.class);
    }

    public CreateCompanyRequest convertCompanyToCreateCompanyRequest(Company company) {
        return modelMapper.map(company, CreateCompanyRequest.class);
    }

    public UpdateCompanyRequest convertCompanyToUpdateCompanyRequest(Company company) {
        return modelMapper.map(company, UpdateCompanyRequest.class);
    }
}
