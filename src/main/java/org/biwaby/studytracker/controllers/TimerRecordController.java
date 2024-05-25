package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimerRecordDTO;
import org.biwaby.studytracker.services.interfaces.TimerRecordService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@AllArgsConstructor
@RequestMapping("/timer")
public class TimerRecordController {

    private final TimerRecordService timerRecordService;

    @GetMapping
    ResponseEntity<Page<TimerRecordDTO>> getAllRecords(@RequestParam int page) {
        return ResponseEntity.ok(timerRecordService.getAllRecords(page));
    }

    @GetMapping("/{recordId}")
    ResponseEntity<TimerRecordDTO> getRecordById(@PathVariable Long recordId) {
        return ResponseEntity.ok(timerRecordService.getRecordById(recordId));
    }

    @PostMapping("/record")
    ResponseEntity<TimerRecordDTO> addRecord(@RequestBody TimerRecordDTO dto) throws ParseException {
        return ResponseEntity.ok(timerRecordService.addRecord(dto));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteRecord(@RequestParam Long recordId) {
        timerRecordService.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    ResponseEntity<Void> editRecord(@RequestParam Long recordId, @RequestBody TimerRecordDTO dto) throws ParseException {
        timerRecordService.editRecord(recordId, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/addTag")
    ResponseEntity<TimerRecordDTO> addTagToRecord(@RequestParam Long recordId, @RequestParam Long tagId) {
        return ResponseEntity.ok(timerRecordService.addTagToRecord(recordId, tagId));
    }

    @PutMapping("/removeTag")
    ResponseEntity<TimerRecordDTO> removeTagFromRecord(@RequestParam Long recordId, @RequestParam Long tagId) {
        return ResponseEntity.ok(timerRecordService.removeTagFromRecord(recordId, tagId));
    }
}
