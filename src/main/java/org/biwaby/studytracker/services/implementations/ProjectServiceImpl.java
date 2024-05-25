package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.ProjectNotFoundException;
import org.biwaby.studytracker.models.DTO.ProjectDTO;
import org.biwaby.studytracker.models.Project;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.ProjectRepo;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.services.interfaces.ProjectService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper mapper;
    private final UserService userService;
    private final RoleRepo roleRepo;

    @Override
    public ProjectDTO createNewProject(ProjectDTO dto) {
        Project project = mapper.toEntity(dto);
        project.setUser(userService.getUserByAuth());
        project = projectRepo.save(project);
        return mapper.toDTO(project);
    }

    @Override
    @Transactional
    public List<ProjectDTO> getAllProjects() {
        List<ProjectDTO> dtos = new ArrayList<>();
        List<Project> projects = projectRepo.findAllByUser(userService.getUserByAuth());
        projects.forEach(project -> dtos.add(mapper.toDTO(project)));
        return dtos;
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(id).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        return mapper.toDTO(project);
    }

    @Override
    public void deleteProject(Long id) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(id).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        projectRepo.delete(project);
    }

    @Override
    public ProjectDTO editProject(Long id, ProjectDTO dto) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Project project = projectRepo.findById(id).orElseThrow(ProjectNotFoundException::new);

        if (!user.getId().equals(project.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        mapper.updateDataFromDTO(project, dto);
        project = projectRepo.save(project);
        return mapper.toDTO(project);
    }
}
