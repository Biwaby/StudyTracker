package org.biwaby.studytracker.utils.MapperUtils;

import org.biwaby.studytracker.models.dto.ProjectDTO;
import org.biwaby.studytracker.models.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectMapper {

    public ProjectDTO toDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());

        if (project.getTitle() != null) {
            dto.setTitle(project.getTitle());
        }
        if (project.getDescription() != null) {
            dto.setDescription(project.getDescription());
        }

        return dto;
    }

    public Project toEntity(ProjectDTO dto) {
        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());

        return project;
    }

    public void updateDataFromDTO(Project project, ProjectDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            project.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            project.setDescription(dto.getDescription());
        }
    }
}
