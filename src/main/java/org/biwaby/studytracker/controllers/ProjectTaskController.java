package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.dto.ProjectTaskDTO;
import org.biwaby.studytracker.services.interfaces.ProjectTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/projects/tasks")
public class ProjectTaskController {

    private final ProjectTaskService projectTaskService;

    @GetMapping
    public ResponseEntity<List<ProjectTaskDTO>> getAllTasksFromProject(@RequestParam Long projectId) {
        return ResponseEntity.ok(projectTaskService.getAllTasksFromProject(projectId));
    }

    @GetMapping("/{projectId}/{taskId}")
    ResponseEntity<ProjectTaskDTO> getTaskByIdFromProject(@PathVariable Long projectId, @PathVariable Long taskId) {
        return ResponseEntity.ok(projectTaskService.getTaskByIdFromProject(projectId, taskId));
    }

    @PostMapping("/add")
    ResponseEntity<ProjectTaskDTO> addTaskToProject(@RequestParam Long projectId, @RequestBody ProjectTaskDTO dto) {
        return ResponseEntity.ok(projectTaskService.addTaskToProject(projectId, dto));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteTaskFromProject(@RequestParam Long projectId, @RequestParam Long taskId) {
        projectTaskService.deleteTaskFromProject(projectId, taskId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    ResponseEntity<ProjectTaskDTO> editTask(@RequestParam Long projectId, @RequestParam Long taskId, @RequestBody ProjectTaskDTO dto) {
        return ResponseEntity.ok(projectTaskService.editTask(projectId, taskId, dto));
    }
}
