package org.biwaby.studytracker.utils.MapperUtils;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.Subject;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.repositories.SubjectRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimerNoteMapper {

    private final SubjectRepo subjectRepo;

    public TimerNoteDTO mapToTimerNoteDTO(TimerNote timerNote) {
        TimerNoteDTO dto = new TimerNoteDTO();
        dto.setSubjectId(timerNote.getSubject().getId());
        dto.setDate(timerNote.getDate());
        dto.setDuration(timerNote.getDuration());
        return dto;
    }

    public TimerNote mapToTimerNoteEntity(TimerNoteDTO dto) {
        TimerNote timerNote = new TimerNote();

        Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
        if (optionalSubject.isPresent()) {
            timerNote.setSubject(optionalSubject.get());
        }
        else {
            timerNote.setSubject(null);
        }
        timerNote.setDate(dto.getDate());
        timerNote.setDuration(dto.getDuration());
        return timerNote;
    }
}
