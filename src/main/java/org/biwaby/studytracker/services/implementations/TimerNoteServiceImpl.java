package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.TimerNoteAlreadyExistsException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.TimerNoteNotFoundException;
import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimerNotePresentationDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TimerNoteRepo;
import org.biwaby.studytracker.services.interfaces.TimerNoteService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.PresentationMappers.TimerNotePresentationMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimerNoteMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimerNoteServiceImpl implements TimerNoteService {

    private final TimerNoteRepo timerNoteRepo;
    private final TimerNoteMapper mapper;
    private final TimerNotePresentationMapper presMapper;
    private final UserService userService;
    private final RoleRepo roleRepo;

    @Override
    public TimerNotePresentationDTO addTimerNote(TimerNoteDTO dto) throws ParseException {
        Optional<TimerNote> optionalTimerNote = timerNoteRepo.findBySubjectId(dto.getSubjectId());
        if (optionalTimerNote.isPresent()) {
            throw new TimerNoteAlreadyExistsException();
        }
        else {
            dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
            TimerNote timerNote = mapper.toEntity(dto);
            timerNoteRepo.save(timerNote);
            return presMapper.toDTO(timerNote);
        }
    }

    @Override
    public List<TimerNotePresentationDTO> getAllTimerNotes() {
        List<TimerNotePresentationDTO> dtos = new ArrayList<>();
        timerNoteRepo.findAll().forEach(obj -> dtos.add(presMapper.toDTO(obj)));
        return dtos;
    }

    @Override
    public TimerNotePresentationDTO getTimerNoteById(Long id) {
        return presMapper.toDTO(timerNoteRepo.findById(id).orElseThrow(TimerNoteNotFoundException::new));
    }

    @Override
    public void deleteTimerNote(Long id) {
        TimerNote timerNote = timerNoteRepo.findById(id).orElseThrow(TimerNoteNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(timerNote.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        timerNoteRepo.delete(timerNote);
    }

    @Override
    public void editTimerNote(Long id, TimerNoteDTO dto) throws ParseException {
        TimerNote timerNote = timerNoteRepo.findById(id).orElseThrow(TimerNoteNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(timerNote.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("Нет доступа");
        }

        mapper.updateDataFromDTO(timerNote, dto);
        timerNoteRepo.save(timerNote);
    }
}
