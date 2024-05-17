package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TaskService {

    TaskPresentationDTO addTask(TaskDTO dto) throws ParseException;

    Page<TaskPresentationDTO> getAllTasks(int page);

    TaskPresentationDTO getTaskById(Long id);

    void deleteTask(Long id);

    TaskPresentationDTO markCompleted(Long id);

    TaskPresentationDTO removeCompleted(Long id);

    void editTask(Long id, TaskDTO dto) throws ParseException;
}
