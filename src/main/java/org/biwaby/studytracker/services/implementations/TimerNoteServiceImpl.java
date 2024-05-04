package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.TimerNote;
import org.biwaby.studytracker.repositories.TimerNoteRepo;
import org.biwaby.studytracker.services.interfaces.TimerNoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeNoteServiceImpl implements TimerNoteService {

    private final TimerNoteRepo timerNoteRepo;

    @Override
    public TimerNote addTimerNote(TimerNote timerNote) {
        return timerNoteRepo.saveAndFlush(timerNote);
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
    public boolean editTimerNote(Long id, TimerNote timerNote) {
        Optional<TimerNote> editableTimerNote = timerNoteRepo.findById(id);
        if (editableTimerNote.isPresent()) {
            TimerNote newTimerNote = editableTimerNote.get();

        }
    }
}
