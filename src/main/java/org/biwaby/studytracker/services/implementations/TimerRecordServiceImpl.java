package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.TagNotFoundException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.TimerRecordNotFoundException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.UserNotFoundException;
import org.biwaby.studytracker.exceptions.RecordAlreadyHasTagException;
import org.biwaby.studytracker.models.DTO.TagDTO;
import org.biwaby.studytracker.models.DTO.TimerRecordDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.TimerRecord;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TagRepo;
import org.biwaby.studytracker.repositories.TimerRecordRepo;
import org.biwaby.studytracker.repositories.UserRepo;
import org.biwaby.studytracker.services.interfaces.TimerRecordService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimerRecordMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public TimerRecordDTO addRecord(TimerRecordDTO dto) throws ParseException {
        TimerRecord record = mapper.toEntity(dto);
        record.setUser(userService.getUserByAuth());
        timerRecordRepo.save(record);
        return TimerRecordMapper.toDTO(record);
    }

    @Override
    public Page<TimerRecordDTO> getAllRecords(int page) {
        User user = userService.getUserByAuth();
        Page<TimerRecord> recordsObjPage = timerRecordRepo.findAllByUser(PageRequest.of(page, 5), user);
        List<TimerRecordDTO> dtoList = new ArrayList<>();

        for (TimerRecord record : recordsObjPage) {
            TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
            record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
            dtoList.add(dto);
        }

        return new PageImpl<>(dtoList);


        //TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        //record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        //return dto;
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
    public void editRecord(Long id, TimerRecordDTO dto) throws ParseException {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(id).orElseThrow(TimerRecordNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        mapper.updateDataFromDTO(record, dto);
        if (dto.getTags() != null) {
            Set<Tag> tags = new HashSet<>();
            dto.getTags().forEach(tagDto -> tags.add(tagMapper.toEntity(tagDto)));
            record.setTags(tags);
        }
        timerRecordRepo.save(record);
    }

    @Override
    public TimerRecordDTO addTagToRecord(Long recordId, Long tagId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);
        Tag tag = tagRepo.findById(tagId).orElseThrow(TagNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (!tag.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (record.getTags().contains(tag)) {
            throw new RecordAlreadyHasTagException();
        }

        record.getTags().add(tag);
        timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public TimerRecordDTO removeTagFromRecord(Long recordId, Long tagId) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        TimerRecord record = timerRecordRepo.findById(recordId).orElseThrow(TimerRecordNotFoundException::new);
        Tag tag = tagRepo.findById(tagId).orElseThrow(TagNotFoundException::new);

        if (!record.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (!tag.getUser().getId().equals(user.getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }
        if (!record.getTags().contains(tag)) {
            throw new TagNotFoundException();
        }

        record.getTags().remove(tag);
        timerRecordRepo.save(record);
        TimerRecordDTO dto = TimerRecordMapper.toDTO(record);
        record.getTags().forEach(tagObj -> dto.getTags().add(tagMapper.toDTO(tagObj)));
        return dto;
    }

    @Override
    public Page<TimerRecord> getAllRecordsByUser(String username, int page) {
        User user = userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return timerRecordRepo.findAllByUser(PageRequest.of(page, 5), user);
    }
}
