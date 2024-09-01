package com.example.technical.controllers;

import com.example.technical.models.dto.CompanyDTO;
import com.example.technical.models.request.CreateCompanyRequest;
import com.example.technical.models.request.UpdateCompanyRequest;
import com.example.technical.services.CompanyService;
import com.example.technical.utils.Mapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final Mapper mapper;

    @PostMapping
    public ResponseEntity<CompanyDTO> create(@RequestBody @Valid CreateCompanyRequest body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.create(mapper
                        .convertCreateCompanyRequestToCompany(body)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(companyService.getById(id));
    }

    @GetMapping
    ResponseEntity<List<CompanyDTO>> getAll() {
        return ResponseEntity.ok(companyService.getAll());
    }

    @PatchMapping
    public ResponseEntity<CompanyDTO> update(@RequestBody @Valid UpdateCompanyRequest body) {
        return ResponseEntity.ok(companyService.update(mapper
                .convertUpdateCompanyRequestToCompany(body)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
