package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.Subject;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.SubjectRepo;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimerNoteMapper {

    private final SubjectRepo subjectRepo;
    private final UserService userService;

    public TimerNoteDTO toDTO(TimerNote timerNote) {
        TimerNoteDTO dto = new TimerNoteDTO();
        dto.setSubjectId(timerNote.getSubject().getId());
        dto.setDate(new SimpleDateFormat("dd-MM-yyyy").format(timerNote.getDate()));
        dto.setDuration(timerNote.getDuration());
        return dto;
    }

    public TimerNote toEntity(TimerNoteDTO dto) throws ParseException {
        TimerNote timerNote = new TimerNote();

        timerNote.setUser(userService.getUserByAuth());
        Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
        if (optionalSubject.isPresent()) {
            timerNote.setSubject(optionalSubject.get());
        }
        else {
            timerNote.setSubject(null);
        }
        timerNote.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDate()));
        timerNote.setDuration(dto.getDuration());
        return timerNote;
    }

    public void updateDataFromDTO(TimerNote timerNote, TimerNoteDTO dto) throws ParseException {
        if (dto == null) {
            return;
        }
        if (dto.getSubjectId() != null) {
            Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
            optionalSubject.ifPresent(timerNote::setSubject);
        }
        if (dto.getDate() != null) {
            timerNote.setDate(new SimpleDateFormat("dd-MM-yyyy").parse(dto.getDate()));
        }
        if (dto.getDuration() != null) {
            timerNote.setDuration(dto.getDuration());
        }
    }
}
