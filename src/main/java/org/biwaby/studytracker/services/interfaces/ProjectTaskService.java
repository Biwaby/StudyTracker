package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.ProjectTaskDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectTaskService {
    ProjectTaskDTO addTaskToProject(Long projectId, ProjectTaskDTO dto);
    List<ProjectTaskDTO> getAllTasksFromProject(Long projectId);
    ProjectTaskDTO getTaskByIdFromProject(Long projectId, Long taskId);
    void deleteTaskFromProject(Long projectId, Long taskId);
    void editTask(Long projectId, Long taskId, ProjectTaskDTO dto);
}
