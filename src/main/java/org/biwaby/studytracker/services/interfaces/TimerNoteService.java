package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimerNotePresentationDTO;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface TimerNoteService {

    TimerNotePresentationDTO addTimerNote(TimerNoteDTO dto) throws ParseException;

    List<TimerNotePresentationDTO> getAllTimerNotes();

    boolean deleteTimerNote(Long id);

    boolean editTimerNote(Long id, TimerNoteDTO dto) throws ParseException;
}
