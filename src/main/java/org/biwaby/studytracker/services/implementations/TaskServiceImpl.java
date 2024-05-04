package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.repositories.TaskRepo;
import org.biwaby.studytracker.services.interfaces.TaskService;
import org.biwaby.studytracker.utils.MapperUtils.TaskMapper;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper mapper;

    @Override
    public Task addTask(TaskDTO dto) throws ParseException {
        return taskRepo.saveAndFlush(mapper.mapToTaskEntity(dto));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public boolean deleteTask(Long id) {
        Optional<Task> task = taskRepo.findById(id);
        if (task.isPresent()) {
            taskRepo.delete(task.get());
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean editTask(Long id, TaskDTO dto) throws ParseException {
        Optional<Task> editableTask = taskRepo.findById(id);
        if (editableTask.isPresent()) {
            Task newTask = editableTask.get();
            Task mappedTask = mapper.mapToTaskEntity(dto);
            newTask.setTitle(mappedTask.getTitle());
            newTask.setDescription(mappedTask.getDescription());
            newTask.setSubject(mappedTask.getSubject());
            newTask.setStatus(mappedTask.isStatus());
            newTask.setDeadlineDate(mappedTask.getDeadlineDate());
            taskRepo.save(newTask);
            return true;
        }
        else {
            return false;
        }
    }
}
