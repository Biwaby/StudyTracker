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

    public TimerNoteDTO toDTO(TimerNote timerNote) {
        TimerNoteDTO dto = new TimerNoteDTO();
        dto.setSubjectId(timerNote.getSubject().getId());
        dto.setDate(timerNote.getDate());
        dto.setDuration(timerNote.getDuration());
        return dto;
    }

    public TimerNote toEntity(TimerNoteDTO dto) {
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

    public void updateDataFromDTO(TimerNote timerNote, TimerNoteDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getSubjectId() != null) {
            Optional<Subject> optionalSubject = subjectRepo.findById(dto.getSubjectId());
            optionalSubject.ifPresent(timerNote::setSubject);
        }
        if (dto.getDate() != null) {
            timerNote.setDate(dto.getDate());
        }
        if (dto.getDuration() != null) {
            timerNote.setDuration(dto.getDuration());
        }
    }
}
