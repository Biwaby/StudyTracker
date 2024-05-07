package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.TimerNoteAlreadyExistsException;
import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.repositories.TimerNoteRepo;
import org.biwaby.studytracker.services.interfaces.TimerNoteService;
import org.biwaby.studytracker.utils.MapperUtils.TimerNoteMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimerNoteServiceImpl implements TimerNoteService {

    private final TimerNoteRepo timerNoteRepo;
    private final TimerNoteMapper mapper;

    @Override
    public TimerNote addTimerNote(TimerNoteDTO dto) {
        Optional<TimerNote> optionalTimerNote = timerNoteRepo.findBySubjectId(dto.getSubjectId());
        if (optionalTimerNote.isPresent()) {
            throw new TimerNoteAlreadyExistsException();
        }
        else {
            dto.setDate(new Date());
            return timerNoteRepo.saveAndFlush(mapper.toEntity(dto));
        }
    }

    @Override
    public List<TimerNote> getAllTimerNotes() {
        return timerNoteRepo.findAll();
    }

    @Override
    public boolean deleteTimerNote(Long id) {
        Optional<TimerNote> timerNote = timerNoteRepo.findById(id);
        if (timerNote.isPresent()) {
            timerNoteRepo.delete(timerNote.get());
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean editTimerNote(Long id, TimerNoteDTO dto) {
        Optional<TimerNote> editableTimerNote = timerNoteRepo.findById(id);
        if (editableTimerNote.isPresent()) {
            TimerNote newTimerNote = editableTimerNote.get();
            mapper.updateDataFromDTO(newTimerNote, dto);
            timerNoteRepo.save(newTimerNote);
            return true;
        }
        return false;
    }
}
