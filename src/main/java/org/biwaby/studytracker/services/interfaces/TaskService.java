package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.Task;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TaskService {

    Task addTask(TaskDTO dto) throws ParseException;

    List<Task> getAllTasks();

    boolean deleteTask(Long id);

    boolean editTask(Long id, TaskDTO dto) throws ParseException;
}
