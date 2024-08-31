package com.example.technical.controller;

import com.example.technical.JsonPrettyPrinter;
import com.example.technical.TechnicalApplication;
import com.example.technical.models.entity.*;
import com.example.technical.models.request.CreateCompanyRequest;
import com.example.technical.models.request.UpdateCompanyRequest;
import com.example.technical.utils.Mapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = {TechnicalApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompanyControllerTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"));

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Mapper mapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void create_Success_Test() throws Exception {
        CreateCompanyRequest body = createCreateCompanyRequest();

        mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()));
    }

    @Test
    void create_BodyWithIncorrectManagerDataFailsValidation_Failure_Test() throws Exception {
        CreateCompanyRequest body = CreateCompanyRequest.builder()
                .name("Some Name")
                .departmentList(List.of(CreateCompanyRequest.Department.builder()
                        .name("Some name")
                        .teamList(List.of(CreateCompanyRequest.Team.builder()
                                .name("Some name")
                                .project(CreateCompanyRequest.Project.builder()
                                        .manager(CreateCompanyRequest.Manager.builder()
                                                .name("")
                                                .surname("")
                                                .email("someEmailgmailcom")
                                                .address("")
                                                .phoneNumber("")
                                                .build())
                                        .build())
                                .build()))
                        .build()))
                .build();

        mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isBadRequest())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.errors['departmentList[0].teamList[0].project.manager.name']")
                        .value("must not be blank"))
                .andExpect(jsonPath("$.errors['departmentList[0].teamList[0].project.manager.surname']")
                        .value("must not be blank"))
                .andExpect(jsonPath("$.errors['departmentList[0].teamList[0].project.manager.email']")
                        .value("must be a well-formed email address"))
                .andExpect(jsonPath("$.errors['departmentList[0].teamList[0].project.manager.address']")
                        .value("must not be blank"))
                .andExpect(jsonPath("$.errors['departmentList[0].teamList[0].project.manager.phoneNumber']")
                        .value("must not be blank"));
    }

    @Test
    void update_ManagerEmail_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();
        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);
        createdCompany.getDepartmentList().get(0).getTeamList().get(0).getProject().getManager().setEmail("newEmail@gmail.com");

        UpdateCompanyRequest updateCompanyRequest = mapper.convertCompanyToUpdateCompanyRequest(createdCompany);

        mockMvc.perform(patch("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isOk())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.departmentList[0].teamList[0].project.manager.email")
                        .value("newEmail@gmail.com"));
    }

    @Test
    void update_AddNewDepartment_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();
        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);
        createdCompany.getDepartmentList().add(Department.builder()
                .name("Some Name")
                .build());

        UpdateCompanyRequest updateCompanyRequest = mapper.convertCompanyToUpdateCompanyRequest(createdCompany);

        mockMvc.perform(patch("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isOk())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.departmentList[1].name")
                        .value("Some Name"));
    }

    @Test
    void update_AddNewDepartmentAndTeam_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();
        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);
        createdCompany.getDepartmentList().add(Department.builder()
                .name("Some Name")
                .teamList(List.of(Team.builder()
                        .name("Some Name")
                        .build()))
                .build());

        UpdateCompanyRequest updateCompanyRequest = mapper.convertCompanyToUpdateCompanyRequest(createdCompany);


        mockMvc.perform(patch("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isOk())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.departmentList[1].teamList[0].name")
                        .value("Some Name"));
    }

    @Test
    void update_AddNewDepartmentAndTeamAndProject_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();

        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);
        createdCompany.getDepartmentList().add(Department.builder()
                .name("Some Name")
                .teamList(List.of(Team.builder()
                        .name("Some Name")
                        .project(Project.builder()
                                .build())
                        .build()))
                .build());

        UpdateCompanyRequest updateCompanyRequest = mapper.convertCompanyToUpdateCompanyRequest(createdCompany);


        mockMvc.perform(patch("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isOk())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.departmentList[1].teamList[0].project.id")
                        .exists());
    }

    @Test
    void update_AddNewDepartmentAndTeamAndProjectAndManager_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();
        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);
        createdCompany.getDepartmentList().add(Department.builder()
                .name("Some Name")
                .teamList(List.of(Team.builder()
                        .name("Some Name")
                        .project(Project.builder()
                                .manager(Manager.builder()
                                        .name("Some name")
                                        .surname("Some surname")
                                        .email("someEmail@gmail.com")
                                        .address("Some address")
                                        .phoneNumber("+48 000 000 000")
                                        .build())
                                .build())
                        .build()))
                .build());

        UpdateCompanyRequest updateCompanyRequest = mapper.convertCompanyToUpdateCompanyRequest(createdCompany);

        mockMvc.perform(patch("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))

                .andExpect(status().isOk())
                .andExpect(result -> JsonPrettyPrinter.print(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.departmentList[1].teamList[0].project.manager.name")
                        .exists())
                .andExpect(jsonPath("$.departmentList.size()").value(2));
    }


    @Test
    void delete_Success_Test() throws Exception {
        CreateCompanyRequest createCompanyRequest = createCreateCompanyRequest();

        String responseBody = mockMvc.perform(post("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCompanyRequest))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        JsonPrettyPrinter.print(responseBody);

        Company createdCompany = objectMapper.readValue(responseBody, Company.class);

        mockMvc.perform(delete("/api/company/" + createdCompany.getId()))
                .andExpect(status().isOk());
        assertFalse(existsById("company", createdCompany.getId()));
        assertFalse(existsById("department", createdCompany.getDepartmentList().get(0).getId()));
        assertFalse(existsById("team", createdCompany.getDepartmentList().get(0).getTeamList().get(0).getId()));
        assertFalse(existsById("project", createdCompany.getDepartmentList().get(0).getTeamList().get(0).getProject().getId()));
        assertFalse(existsById("manager", createdCompany.getDepartmentList().get(0).getTeamList().get(0).getProject().getManager().getId()));
    }

    private CreateCompanyRequest createCreateCompanyRequest() {
        return mapper.convertCompanyToCreateCompanyRequest(Company.builder()
                .name("Some Name")
                .departmentList(List.of(Department.builder()
                        .name("Some name")
                        .teamList(List.of(Team.builder()
                                .name("Some name")
                                .project(Project.builder()
                                        .manager(Manager.builder()
                                                .name("Some name")
                                                .surname("Some surname")
                                                .email("someEmail@gmail.com")
                                                .address("Some address")
                                                .phoneNumber("+48 000 000 000")
                                                .build())
                                        .build())
                                .build()))
                        .build()))
                .build());
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean existsById(String tableName, UUID id) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }
}
