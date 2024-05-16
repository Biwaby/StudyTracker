package org.biwaby.studytracker.utils.MapperUtils.PresentationMappers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class TaskPresentationMapper {

    private final UserService userService;

    public static TaskPresentationDTO toDTO(Task task) {
        TaskPresentationDTO dto = new TaskPresentationDTO();
        dto.setId(task.getId());
        dto.setUser(task.getUser().getUsername());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setSubject(task.getSubject());
        dto.setCompleted(task.isCompleted());
        if (task.getCompletionDate() != null) {
            dto.setCompletionDate(new SimpleDateFormat("dd-MM-yyyy").format(task.getCompletionDate()));
        }
        dto.setDeadlineDate(new SimpleDateFormat("dd-MM-yyyy").format(task.getDeadlineDate()));
        return dto;
    }
}
