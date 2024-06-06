package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.dto.TimerRecordDTO;
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
    public ResponseEntity<Page<TimerRecordDTO>> getAllRecords(@RequestParam int page) {
        return ResponseEntity.ok(timerRecordService.getAllRecords(page));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<TimerRecordDTO> getRecordById(@PathVariable Long recordId) {
        return ResponseEntity.ok(timerRecordService.getRecordById(recordId));
    }

    @PostMapping("/record")
    public ResponseEntity<TimerRecordDTO> addRecord(@RequestBody TimerRecordDTO dto) throws ParseException {
        return ResponseEntity.ok(timerRecordService.addRecord(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRecord(@RequestParam Long recordId) {
        timerRecordService.deleteRecord(recordId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<TimerRecordDTO> editRecord(@RequestParam Long recordId, @RequestBody TimerRecordDTO dto) throws ParseException {
        return ResponseEntity.ok(timerRecordService.editRecord(recordId, dto));
    }

    @PutMapping("/addTag")
    public ResponseEntity<TimerRecordDTO> addTagToRecord(@RequestParam Long recordId, @RequestParam Long tagId) {
        return ResponseEntity.ok(timerRecordService.addTagToRecord(recordId, tagId));
    }

    @PutMapping("/removeTag")
    public ResponseEntity<TimerRecordDTO> removeTagFromRecord(@RequestParam Long recordId, @RequestParam Long tagId) {
        return ResponseEntity.ok(timerRecordService.removeTagFromRecord(recordId, tagId));
    }

    @PutMapping("/addProject")
    public ResponseEntity<TimerRecordDTO> addProjectToRecord(@RequestParam Long recordId, @RequestParam Long projectId) {
        return ResponseEntity.ok(timerRecordService.addProjectToRecord(recordId, projectId));
    }

    @PutMapping("/removeProject")
    public ResponseEntity<TimerRecordDTO> removeProjectFromRecord(@RequestParam Long recordId) {
        return ResponseEntity.ok(timerRecordService.removeProjectFromRecord(recordId));
    }

    @PutMapping("/addTask")
    public ResponseEntity<TimerRecordDTO> addTaskToRecord(@RequestParam Long recordId, @RequestParam Long taskId) {
        return ResponseEntity.ok(timerRecordService.addTaskToRecord(recordId, taskId));
    }

    @PutMapping("/removeTask")
    public ResponseEntity<TimerRecordDTO> removeTaskFromRecord(@RequestParam Long recordId) {
        return ResponseEntity.ok(timerRecordService.removeTaskFromRecord(recordId));
    }
}
