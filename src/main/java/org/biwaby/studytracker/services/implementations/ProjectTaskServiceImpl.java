package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.ProjectTaskNotFoundException;
import org.biwaby.studytracker.models.DTO.ProjectTaskDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.ProjectTask;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.ProjectTaskRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.services.interfaces.ProjectTaskService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectTaskMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {

    private final ProjectTaskRepo projectTaskRepo;
    private final ProjectTaskMapper mapper;
    private final UserService userService;
    private final ProjectRepo projectRepo;
    private final RoleRepo roleRepo;

    @Override
    public ProjectTaskDTO addTaskToProject(Long projectId, ProjectTaskDTO dto) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        ProjectTask task = mapper.toEntity(dto);
        task.setProject(project);
        projectTaskRepo.save(task);
        return mapper.toDTO(task);
    }

    @Override
    @Transactional
    public List<ProjectTaskDTO> getAllTasksFromProject(Long projectId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        List<ProjectTaskDTO> dtos = new ArrayList<>();
        List<ProjectTask> tasks = projectTaskRepo.findAllByProject(project);
        tasks.forEach(task -> dtos.add(mapper.toDTO(task)));
        return dtos;
    }

    @Override
    public ProjectTaskDTO getTaskByIdFromProject(Long projectId, Long taskId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        ProjectTask task = projectTaskRepo.findById(taskId).orElseThrow(ProjectTaskNotFoundException::new);

        if (!project.getId().equals(task.getProject().getId())) {
            throw new AccessDeniedException("No access");
        }

        return mapper.toDTO(task);
    }

    @Override
    public void deleteTaskFromProject(Long projectId, Long taskId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        ProjectTask task = projectTaskRepo.findById(taskId).orElseThrow(ProjectTaskNotFoundException::new);

        if (!project.getId().equals(task.getProject().getId())) {
            throw new AccessDeniedException("No access");
        }

        projectTaskRepo.delete(task);
    }

    @Override
    public void editTask(Long projectId, Long taskId, ProjectTaskDTO dto) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        ProjectTask task = projectTaskRepo.findById(taskId).orElseThrow(ProjectTaskNotFoundException::new);

        if (!project.getId().equals(task.getProject().getId())) {
            throw new AccessDeniedException("No access");
        }

        mapper.updateDataFromDTO(task, dto);
        projectTaskRepo.save(task);
    }
}
