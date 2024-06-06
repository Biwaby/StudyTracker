package org.biwaby.studytracker.services;

import org.biwaby.studytracker.exceptions.notFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.exceptions.notFoundExceptions.ProjectTaskNotFoundException;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.dto.ProjectTaskDTO;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.implementations.ProjectTaskServiceImpl;
import org.biwaby.studytracker.services.implementations.UserServiceImpl;
import org.biwaby.studytracker.services.interfaces.ProjectTaskService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectTaskMapper;
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
public class ProjectTaskServiceTest {

    private static final UserRepo userRepo = Mockito.mock(UserRepo.class);
    private static final RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
    private static final TagRepo tagRepo = Mockito.mock(TagRepo.class);
    private static final ProjectRepo projectRepo = Mockito.mock(ProjectRepo.class);
    private static final ProjectTaskRepo projectTaskRepo = Mockito.mock(ProjectTaskRepo.class);
    private static final TimerRecordRepo timerRecordRepo = Mockito.mock(TimerRecordRepo.class);

    private final ProjectTaskMapper mapper = new ProjectTaskMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserService userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder, tagRepo, projectRepo, projectTaskRepo, timerRecordRepo);
    private final ProjectTaskService projectTaskService = new ProjectTaskServiceImpl(projectTaskRepo, mapper, userService, projectRepo, roleRepo);

    @Test
    @UseMockWithCustomUser
    void addTaskToProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);
        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "test task", "test task desc", false);
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectTask savedTask = new ProjectTask(null, existingProject, "test task", "test task desc", false);
        ProjectTask expectedTask = new ProjectTask(1L, existingProject, "test task", "test task desc", false);

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.save(savedTask)).thenReturn(expectedTask);
        assertEquals(expectedDTO, projectTaskService.addTaskToProject(1L, inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void addTaskToNonExistingProject() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectTaskService.addTaskToProject(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void addTaskToOtherUserProject() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectTaskService.addTaskToProject(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void getAllTasksFromProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        List<ProjectTaskDTO> expectedList = List.of(
                new ProjectTaskDTO(1L, "test task 1", "test task 1 desc", false),
                new ProjectTaskDTO(2L, "test task 2", "test task 2 desc", false)
        );

        List<ProjectTask> foundList = List.of(
                new ProjectTask(1L, existingProject, "test task 1", "test task 1 desc", false),
                new ProjectTask(2L, existingProject, "test task 2", "test task 2 desc", false)
        );

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findAllByProject(existingProject)).thenReturn(foundList);
        assertEquals(expectedList, projectTaskService.getAllTasksFromProject(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getAllTasksFromNonExistingProject() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> projectTaskService.getAllTasksFromProject(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getAllTasksFromOtherUserProject() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectTaskService.getAllTasksFromProject(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getTaskByIdFromProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "test task", "test task desc", false);
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectTask foundTask = new ProjectTask(1L, existingProject, "test task", "test task desc", false);

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.of(foundTask));
        assertEquals(expectedDTO, projectTaskService.getTaskByIdFromProject(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void getTaskByNonExistingIdFromProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectTaskNotFoundException.class, () -> projectTaskService.getTaskByIdFromProject(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void getTaskByIdFromOtherUserProject() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectTaskService.getTaskByIdFromProject(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteTaskFromProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectTask existingTask = new ProjectTask(1L, existingProject, "test task", "test task desc", false);

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.of(existingTask));
        projectTaskService.deleteTaskFromProject(1L, 1L);
        Mockito.verify(projectTaskRepo, Mockito.times(1)).delete(existingTask);
    }

    @Test
    @UseMockWithCustomUser
    void deleteNonExistingTaskFromProject() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectTaskNotFoundException.class, () -> projectTaskService.deleteTaskFromProject(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteTaskFromOtherUserProject() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectTaskService.deleteTaskFromProject(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void editTask() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectTaskDTO inputDTO = new ProjectTaskDTO(null, "test task", "test task desc", false);
        ProjectTaskDTO expectedDTO = new ProjectTaskDTO(1L, "super duper test task", "super duper test task desc", false);
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectTask existingTask = new ProjectTask(1L, existingProject, "test task", "test task desc", false);
        ProjectTask expectedTask = new ProjectTask(1L, existingProject, "super duper test task", "super duper test task desc", false);

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.of(existingTask));
        Mockito.when(projectTaskRepo.save(existingTask)).thenReturn(expectedTask);
        assertEquals(expectedDTO, projectTaskService.editTask(1L, 1L, inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void editForNonExistingTask() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(projectTaskRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectTaskNotFoundException.class, () -> projectTaskService.editTask(1L, 1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void editTaskForOtherUserProject() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Project existingProject = new Project(1L, otherUser, "test project", "test project desc", new ArrayList<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        assertThrows(AccessDeniedException.class, () -> projectTaskService.editTask(1L, 1L, null));
    }
}
