package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.Subject;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.repositories.SubjectRepo;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final SubjectRepo subjectRepo;

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setSubjectId(task.getSubject().getId());
        dto.setStatus(task.isStatus());
        dto.setDeadlineDate(new SimpleDateFormat("dd-MM-yyyy").format(task.getDeadlineDate()));
        return dto;
    }

    public Task toEntity(TaskDTO dto) throws ParseException {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
        if (optionalSubject.isPresent()) {
            task.setSubject(optionalSubject.get());
        }
        else {
            task.setSubject(null);
        }
        task.setStatus(dto.isStatus());
        task.setDeadlineDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDeadlineDate()));
        return task;
    }

    public void updateDataFromDTO(Task task, TaskDTO dto) throws ParseException {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getSubjectId() != null) {
            Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
            optionalSubject.ifPresent(task::setSubject);
        }
        if (dto.getDeadlineDate() != null) {
            task.setDeadlineDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDeadlineDate()));
        }
    }
}
