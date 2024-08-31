package com.example.technical.models.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompanyRequest {
    @Valid
    @NotBlank
    private String name;
    @Valid
    @NotEmpty
    private List<Department> departmentList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Department {
        @Valid
        @NotBlank
        private String name;
        @Valid
        @NotEmpty
        private List<Team> teamList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Team {
        @Valid
        @NotBlank
        private String name;
        @Valid
        private Project project;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Project {
        @Valid
        private Manager manager;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Manager {
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