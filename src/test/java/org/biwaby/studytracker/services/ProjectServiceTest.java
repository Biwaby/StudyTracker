package org.biwaby.studytracker.services;

import org.biwaby.studytracker.exceptions.NotFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.models.DTO.ProjectDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.implementations.ProjectServiceImpl;
import org.biwaby.studytracker.services.implementations.UserServiceImpl;
import org.biwaby.studytracker.services.interfaces.ProjectService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.biwaby.studytracker.utils.UseMockWithCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class ProjectServiceTest {

    private static final UserRepo userRepo = Mockito.mock(UserRepo.class);
    private static final RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
    private static final TagRepo tagRepo = Mockito.mock(TagRepo.class);
    private static final ProjectRepo projectRepo = Mockito.mock(ProjectRepo.class);
    private static final ProjectTaskRepo projectTaskRepo = Mockito.mock(ProjectTaskRepo.class);
    private static final TimerRecordRepo timerRecordRepo = Mockito.mock(TimerRecordRepo.class);

    private final ProjectMapper mapper = new ProjectMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserService userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder, tagRepo, projectRepo, projectTaskRepo, timerRecordRepo);
    private final ProjectService projectService = new ProjectServiceImpl(projectRepo, mapper, userService, roleRepo);

    @Test
    @UseMockWithCustomUser
    void createNewProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectDTO inputDTO = new ProjectDTO(null, "test project", "test project desc");
        ProjectDTO expectedDTO = new ProjectDTO(1L, "test project", "test project desc");
        Project savedProject = new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>());
        Project expectedProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(projectRepo.save(savedProject)).thenReturn(expectedProject);
        assertEquals(expectedDTO, projectService.createNewProject(inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void getAllProjects() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ProjectDTO> expectedList = List.of(
                new ProjectDTO(1L, "test project 1", "test project desc 1"),
                new ProjectDTO(2L, "test project 2", "test project desc 2")
        );

        List<Project> foundList = List.of(
                new Project(1L, sessionUser, "test project 1", "test project desc 1", new ArrayList<>()),
                new Project(2L, sessionUser, "test project 2", "test project desc 2", new ArrayList<>())
        );

        Mockito.when(projectRepo.findAllByUser(sessionUser)).thenReturn(foundList);
        assertEquals(expectedList, projectService.getAllProjects());
    }

    @Test
    @UseMockWithCustomUser
    void getProjectById() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectDTO expectedDTO = new ProjectDTO(1L, "test project", "test project desc");
        Project foundProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(foundProject));
        assertEquals(expectedDTO, projectService.getProjectById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getProjectByNonExistingId() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getProjectByIdForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project foundProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(foundProject));
        assertThrows(AccessDeniedException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        projectService.deleteProject(1L);
        Mockito.verify(projectRepo, Mockito.times(1)).delete(existingProject);
    }

    @Test
    @UseMockWithCustomUser
    void deleteNonExistingProject() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.deleteProject(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteProjectForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectService.deleteProject(1L));
    }

    @Test
    @UseMockWithCustomUser
    void editProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectDTO inputDTO = new ProjectDTO(null, "test project", "test project desc");
        ProjectDTO expectedDTO = new ProjectDTO(1L, "super duper test project", "super duper test project desc");
        Project existingProject = new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>());
        Project expectedProject = new Project(1L, sessionUser, "super duper test project", "super duper test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectRepo.save(existingProject)).thenReturn(expectedProject);
        assertEquals(expectedDTO, projectService.editProject(1L, inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void editForNonExistingProject() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectService.editProject(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void editProjectForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectService.editProject(1L, null));
    }
}
