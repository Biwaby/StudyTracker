package org.biwaby.studytracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biwaby.studytracker.models.DTO.ProjectDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.UserRepo;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectControllerTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ProjectRepo  projectRepo;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setParams() {
        Role userRole = roleRepo.save(new Role(null, "USER"));
        Role adminRole = roleRepo.save(new Role(null, "ADMIN"));

        User sessionUser = new User(
                null,
                "testUser",
                passwordEncoder.encode("1234"),
                true,
                new HashSet<>(List.of(userRole))
        );
        sessionUser = userRepo.save(sessionUser);

        User otherUser = new User(
                null,
                "otherTestUser",
                passwordEncoder.encode("1234"),
                true,
                new HashSet<>(List.of(userRole))
        );
        otherUser = userRepo.save(otherUser);
    }

    @Test
    void createNewProject() throws Exception {
        ProjectDTO inputDTO = new ProjectDTO(null, "test project", "test project desc");
        ProjectDTO expectedDTO = new ProjectDTO(1L, "test project", "test project desc");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/projects/create")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void getAllProjects() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Project project1 = projectRepo.save(new Project(null, sessionUser, "test project 1", "test project 1 desc", new ArrayList<>()));
        Project project2 = projectRepo.save(new Project(null, sessionUser, "test project 2", "test project 2 desc", new ArrayList<>()));

        List<ProjectDTO> expectedDTOList = List.of(
                projectMapper.toDTO(project1),
                projectMapper.toDTO(project2)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTOList)));
    }

    @Test
    void getProjectById() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectDTO foundDTO = new ProjectDTO(1L, "test project", "test project desc");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(foundDTO)));
    }

    @Test
    void getNonExistingProject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getProjectForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/projects/delete?projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteNonExistingProject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/projects/delete?projectId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteProjectForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/projects/delete?projectId=1")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void editProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        ProjectDTO inputDTO = new ProjectDTO(null, "super duper test project", "super duper test project desc");
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectDTO expectedDTO = new ProjectDTO(1L, "super duper test project", "super duper test project desc");

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/projects/edit?projectId=1")
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void editNonExistingProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectDTO inputDTO = new ProjectDTO(null, "super duper test project", "super duper test project desc");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/projects/edit?projectId=55")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editProjectForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));
        ProjectDTO inputDTO = new ProjectDTO(null, "super duper test project", "super duper test project desc");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/projects/edit?projectId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
