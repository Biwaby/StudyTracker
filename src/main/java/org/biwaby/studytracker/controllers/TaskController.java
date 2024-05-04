package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TaskDTO;
import org.biwaby.studytracker.models.Task;
import org.biwaby.studytracker.services.interfaces.TaskService;
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
    ResponseEntity<Task> addTask(@RequestBody TaskDTO dto) throws ParseException {
        return ResponseEntity.ok(taskService.addTask(dto));
    }

    @GetMapping
    ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editTask(@PathVariable Long id, @RequestBody TaskDTO dto) throws ParseException {
        if (taskService.editTask(id, dto)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
