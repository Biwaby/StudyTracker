package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.TaskAlreadyCompletedException;
import org.biwaby.studytracker.exceptions.TaskHasNotBeenCompletedAnywayException;
import org.biwaby.studytracker.exceptions.TaskNotFoundException;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.TaskRepo;
import org.biwaby.studytracker.services.interfaces.TaskService;
import org.biwaby.studytracker.utils.MapperUtils.PresentationMappers.TaskPresentationMapper;
import org.biwaby.studytracker.utils.MapperUtils.TaskMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper mapper;
    private final TaskPresentationMapper presMapper;

    @Override
    public TaskPresentationDTO addTask(TaskDTO dto) throws ParseException {
        Task task = mapper.toEntity(dto);
        taskRepo.saveAndFlush(task);
        return presMapper.toDTO(task);
    }

    private static <T, R> Page<R> mapPage(Page<T> page, Function<T, R> mapper) {
        List<R> newItems = page.stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageImpl<>(newItems);
    }

    @Override
    public Page<TaskPresentationDTO> getAllTasks(int page) {
        Page<Task> taskPage = taskRepo.findAll(PageRequest.of(page, 5));
        Page<TaskPresentationDTO> dtoPage = mapPage(taskPage, TaskPresentationMapper::toDTO);
        return dtoPage;
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
    public TaskPresentationDTO markCompleted(Long id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        if (optionalTask.isPresent()) {
            if (optionalTask.get().isCompleted()) {
                throw new TaskAlreadyCompletedException();
            }
            Task editableTask = optionalTask.get();
            editableTask.setCompleted(true);
            editableTask.setCompletionDate(new Date());
            taskRepo.save(editableTask);
            return presMapper.toDTO(editableTask);
        }
        throw new TaskNotFoundException();
    }

    @Override
    public TaskPresentationDTO removeCompleted(Long id) {
        Optional<Task> optionalTask = taskRepo.findById(id);
        if (optionalTask.isPresent()) {
            if (!optionalTask.get().isCompleted()) {
                throw new TaskHasNotBeenCompletedAnywayException();
            }
            Task editableTask = optionalTask.get();
            editableTask.setCompleted(false);
            editableTask.setCompletionDate(null);
            taskRepo.save(editableTask);
            return presMapper.toDTO(editableTask);
        }
        throw new TaskNotFoundException();
    }

    @Override
    public boolean editTask(Long id, TaskDTO dto) throws ParseException {
        Optional<Task> editableTask = taskRepo.findById(id);
        if (editableTask.isPresent()) {
            Task newTask = editableTask.get();
            mapper.updateDataFromDTO(newTask, dto);
            taskRepo.save(newTask);
            return true;
        }
        else {
            return false;
        }
    }
}
