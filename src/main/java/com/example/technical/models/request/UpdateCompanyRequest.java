package com.example.technical.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompanyRequest {
    private UUID id;
    @Valid
    @NotBlank
    private String name;
    @Valid
    private List<Department> departmentList = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Department {
        private UUID id;
        @Valid
        @NotBlank
        private String name;
        @Valid
        private List<Team> teamList;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Team {
        private UUID id;
        @Valid
        @NotBlank
        private String name;
        @Valid
        private Project project;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project {
        private UUID id;
        @Valid
        private Manager manager;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Manager {
        private UUID id;
        @Valid
        @NotBlank
        private String name;
        @Valid
        @NotBlank
        private String surname;
        @Valid
        @NotBlank
        @Email
        private String email;
        @Valid
        @NotBlank
        private String address;
        @Valid
        @NotBlank
        private String phoneNumber;
    }
}