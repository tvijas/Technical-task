package com.example.technical.config;

import com.example.technical.models.dto.*;
import com.example.technical.models.entity.*;
import com.example.technical.models.request.CreateCompanyRequest;
import com.example.technical.models.request.UpdateCompanyRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Company.class, CompanyDTO.class);

        modelMapper.createTypeMap(Company.class, UpdateCompanyRequest.class);

        modelMapper.createTypeMap(Company.class, CreateCompanyRequest.class);

        return modelMapper;
    }
}
