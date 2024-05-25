package org.biwaby.studytracker.utils.MapperUtils;

import org.biwaby.studytracker.models.DTO.ProjectTaskDTO;
import org.biwaby.studytracker.models.ProjectTask;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskMapper {

    public ProjectTaskDTO toDTO(ProjectTask task) {
        ProjectTaskDTO dto = new ProjectTaskDTO();
        dto.setId(task.getId());

        if (task.getTitle() != null) {
            dto.setTitle(task.getTitle());
        }
        if (task.getDescription() != null) {
            dto.setDescription(task.getDescription());
        }
        if (task.isCompleted()) {
            dto.setCompleted(true);
        }

        return dto;
    }

    public ProjectTask toEntity(ProjectTaskDTO dto) {
        ProjectTask task = new ProjectTask();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());

        return task;
    }

    public void updateDataFromDTO(ProjectTask task, ProjectTaskDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.isCompleted()) {
            task.setCompleted(true);
        }
    }
}
