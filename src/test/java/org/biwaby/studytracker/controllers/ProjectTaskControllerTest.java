package org.biwaby.studytracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biwaby.studytracker.models.DTO.ProjectTaskDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.ProjectTask;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.ProjectTaskRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.UserRepo;
import org.biwaby.studytracker.utils.MapperUtils.ProjectTaskMapper;
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
public class ProjectTaskControllerTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private ProjectTaskRepo projectTaskRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ProjectTaskMapper projectTaskMapper;
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
    void addTaskToProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "test task", "test task desc", false);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/projects/tasks/add?projectId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void addTaskToNonExistingProject() throws Exception {
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/projects/tasks/add?projectId=55")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addTaskToOtherUserProject() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/projects/tasks/add?projectId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getAllTasksFromProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));

        List<ProjectTaskDTO> expectedDTOList = List.of(
                projectTaskMapper.toDTO(projectTaskRepo.save(new ProjectTask(null, existingProject, "test task 1", "test task 1 desc", false))),
                projectTaskMapper.toDTO(projectTaskRepo.save(new ProjectTask(null, existingProject, "test task 2", "test task 2 desc", false)))
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks?projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTOList)));
    }

    @Test
    void getAllTasksFromNonExistingProject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks?projectId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getAllTasksFromOtherUserProject() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks?projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getTaskByIdFromProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        projectTaskRepo.save(new ProjectTask(null, existingProject, "test task", "test task desc", false));

        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "test task", "test task desc", false);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks/1/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void getNonExistingTaskFromProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks/1/55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTaskFromOtherUserProject() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/projects/tasks/1/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteTaskFromProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        projectTaskRepo.save(new ProjectTask(null, existingProject, "test task", "test task desc", false));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/projects/tasks/delete?projectId=1&taskId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteNonExistingTaskFromProject() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/projects/tasks/delete?projectId=1&taskId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTaskFromOtherUserProject() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/projects/tasks/delete?projectId=1&taskId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void editTask() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "super duper test task", "super duper test task desc", true);
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        projectTaskRepo.save(new ProjectTask(null, existingProject, "test task", "test task desc", false));
        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "super duper test task", "super duper test task desc", true);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/projects/tasks/edit?projectId=1&taskId=1")
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void editNonExistingTask() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/projects/tasks/edit?projectId=1&taskId=55")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editTaskForOtherUserProject() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        projectRepo.save(new Project(null, otherUser, "test project", "test project desc", new ArrayList<>()));
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/projects/tasks/edit?projectId=1&taskId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
 }
