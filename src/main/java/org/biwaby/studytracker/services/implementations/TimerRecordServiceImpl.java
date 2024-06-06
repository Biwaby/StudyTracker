package org.biwaby.studytracker.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.*;
import org.biwaby.studytracker.exceptions.notFoundExceptions.*;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.dto.TimerRecordDTO;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.interfaces.TimerRecordService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.biwaby.studytracker.utils.MapperUtils.ProjectTaskMapper;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimerRecordMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimerRecordServiceImpl implements TimerRecordService {

    private final TimerRecordRepo timerRecordRepo;
    private final TimerRecordMapper mapper;
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final TagRepo tagRepo;
    private final TagMapper tagMapper;
    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;
    private final ProjectTaskMapper projectTaskMapper;
    private final ProjectTaskRepo projectTaskRepo;

    @Override
    public TimerRecordDTO addRecord(TimerRecordDTO dto) throws ParseException {
        TimerRecord record = mapper.toEntity(dto);
        record.setUser(userService.getUserByAuth());
        record = timerRecordRepo.save(record);
        return TimerRecordMapper.toDTO(record);
    }

    @Override
    @Transactional
    public Page<TimerRecordDTO> getAllRecords(int page) {
        User user = userService.getUserByAuth();
        Page<TimerRecord> recordsObjPage = timerRecordRepo.findAllByUser(PageRequest.of(page, 5), user);
        List<TimerRecordDTO> dtoList = new ArrayList<>();

        for (TimerRecord record : recordsObjPage) {
            TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
            if (record.getProject() != null) {
                dto.setProject(projectMapper.toDTO(record.getProject()));
            }
            if (record.getProjectTask() != null) {
                dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
            }
            record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList, PageRequest.of(recordsObjPage.getPageable().getPageNumber(), recordsObjPage.getPageable().getPageSize()), recordsObjPage.getTotalElements());
    }

    @Override
    public TimerRecordDTO getRecordById(Long id) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(id).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public void deleteRecord(Long id) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(id).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        timerRecordRepo.delete(record);
    }

    @Override
    public TimerRecordDTO editRecord(Long id, TimerRecordDTO dto) throws ParseException {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(id).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        mapper.updateDataFromDTO(record, dto);

        record = timerRecordRepo.save(record);
        TimerRecordDTO recordDTO = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            recordDTO.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            recordDTO.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> recordDTO.getTags().add(tagMapper.toDTO(tagObj)));

        return recordDTO;
    }

    @Override
    public TimerRecordDTO addProjectToRecord(Long recordId, Long projectId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        Project project = projectRepo.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        if (!project.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (record.getProject() != null) {
            throw new RecordAlreadyHasProjectException();
        }

        record.setProject(project);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO removeProjectFromRecord(Long recordId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        if (record.getProject() == null) {
            throw new RecordDoesNotHaveProjectException();
        }

        record.setProject(null);
        record.setProjectTask(null);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO addTaskToRecord(Long recordId, Long taskId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        Project project = record.getProject();

        if (project == null) {
            throw new RecordDoesNotHaveProjectException();
        }
        if (!project.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        ProjectTask task = projectTaskRepo.findById(taskId).orElseThrow(ProjectTaskNotFoundException::new);

        if (!project.getId().equals(task.getProject().getId())) {
            throw new AccessDeniedException("No access");
        }

        if (task.isCompleted()) {
            throw new ProjectTaskAlreadyCompletedException();
        }

        record.setProjectTask(task);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO removeTaskFromRecord(Long recordId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        Project project = record.getProject();

        if (project == null) {
            throw new RecordDoesNotHaveProjectException();
        }
        if (!project.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        if (record.getProjectTask() == null) {
            throw new RecordDoesNotHaveProjectTaskException();
        }

        record.setProjectTask(null);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO addTagToRecord(Long recordId, Long tagId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        Tag tag = tagRepo.findById(tagId).orElseThrow(TagNotFoundException::new);

        if (!tag.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (record.getTags().contains(tag)) {
            throw new RecordAlreadyHasTagException();
        }

        record.getTags().add(tag);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO removeTagFromRecord(Long recordId, Long tagId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        Tag tag = tagRepo.findById(tagId).orElseThrow(TagNotFoundException::new);

        if (!tag.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (!record.getTags().contains(tag)) {
            throw new RecordDoesNotHaveTagException();
        }

        record.getTags().remove(tag);
        record = timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        if (record.getProject() != null) {
            dto.setProject(projectMapper.toDTO(record.getProject()));
        }
        if (record.getProjectTask() != null) {
            dto.setProjectTask(projectTaskMapper.toDTO(record.getProjectTask()));
        }
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public Page<TimerRecord> getAllRecordsByUser(String username, int page) {
        User user = userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return timerRecordRepo.findAllByUser(PageRequest.of(page, 5), user);
    }
}
