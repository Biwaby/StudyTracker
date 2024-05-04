package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.TimerNote;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimerNoteService {

    TimerNote addTimerNote(TimerNoteDTO dto);

    List<TimerNote> getAllTimerNotes();

    boolean deleteTimerNote(Long id);

    boolean editTimerNote(Long id, TimerNoteDTO dto);
}
