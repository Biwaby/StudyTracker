package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimerNoteDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimerNotePresentationDTO;
import org.biwaby.studytracker.services.interfaces.TimerNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timernotes")
public class TimerNoteController {

    private final TimerNoteService timerNoteService;

    @PostMapping
    ResponseEntity<TimerNotePresentationDTO> addTimerNote(@RequestBody TimerNoteDTO dto) throws ParseException {
        return ResponseEntity.ok(timerNoteService.addTimerNote(dto));
    }

    @GetMapping
    ResponseEntity<List<TimerNotePresentationDTO>> getAllTimerNotes() {
        return ResponseEntity.ok(timerNoteService.getAllTimerNotes());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTimerNote(@PathVariable Long id) {
        if (timerNoteService.deleteTimerNote(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editTimerNote(@PathVariable Long id, @RequestBody TimerNoteDTO dto) throws ParseException {
        if (timerNoteService.editTimerNote(id, dto)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
