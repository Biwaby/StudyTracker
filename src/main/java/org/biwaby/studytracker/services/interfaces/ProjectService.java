package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.dto.ProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    ProjectDTO createNewProject(ProjectDTO dto);
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long id);
    void deleteProject(Long id);
    ProjectDTO editProject(Long id, ProjectDTO dto);
}
