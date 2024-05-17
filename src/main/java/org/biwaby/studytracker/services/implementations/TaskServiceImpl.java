package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.TaskAlreadyCompletedException;
import org.biwaby.studytracker.exceptions.TaskHasNotBeenCompletedAnywayException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.TaskNotFoundException;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TaskRepo;
import org.biwaby.studytracker.services.interfaces.TaskService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.PresentationMappers.TaskPresentationMapper;
import org.biwaby.studytracker.utils.MapperUtils.TaskMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper mapper;
    private final TaskPresentationMapper presMapper;
    private final UserService userService;
    private final RoleRepo roleRepo;

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
        return mapPage(taskPage, TaskPresentationMapper::toDTO);
    }

    @Override
    public TaskPresentationDTO getTaskById(Long id) {
        return presMapper.toDTO(taskRepo.findById(id).orElseThrow(TaskNotFoundException::new));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(TaskNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(task.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        taskRepo.delete(task);
    }

    @Override
    public TaskPresentationDTO markCompleted(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(TaskNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(task.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }
        if (task.isCompleted()) {
            throw new TaskAlreadyCompletedException();
        }

        task.setCompleted(true);
        task.setCompletionDate(new Date());
        taskRepo.save(task);
        return presMapper.toDTO(task);
    }

    @Override
    public TaskPresentationDTO removeCompleted(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(TaskNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(task.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }
        if (!task.isCompleted()) {
            throw new TaskHasNotBeenCompletedAnywayException();
        }

        task.setCompleted(false);
        task.setCompletionDate(null);
        taskRepo.save(task);
        return presMapper.toDTO(task);
    }

    @Override
    public void editTask(Long id, TaskDTO dto) throws ParseException {
        Task task = taskRepo.findById(id).orElseThrow(TaskNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(task.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        mapper.updateDataFromDTO(task, dto);
        taskRepo.save(task);
    }
}
