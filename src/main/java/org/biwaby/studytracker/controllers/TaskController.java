package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
import org.biwaby.studytracker.services.interfaces.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    ResponseEntity<TaskPresentationDTO> addTask(@RequestBody TaskDTO dto) throws ParseException {
        return ResponseEntity.ok(taskService.addTask(dto));
    }

    @GetMapping
    ResponseEntity<Page<TaskPresentationDTO>> getAllTasks(@RequestParam int page) {
        return ResponseEntity.ok(taskService.getAllTasks(page));
    }

    @GetMapping("/{id}")
    ResponseEntity<TaskPresentationDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markCompleted/{id}")
    ResponseEntity<TaskPresentationDTO> markCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.markCompleted(id));
    }

    @PutMapping("/removeCompleted/{id}")
    ResponseEntity<TaskPresentationDTO> removeCompleted(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.removeCompleted(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editTask(@PathVariable Long id, @RequestBody TaskDTO dto) throws ParseException {
        taskService.editTask(id, dto);
        return ResponseEntity.ok().build();
    }
}
