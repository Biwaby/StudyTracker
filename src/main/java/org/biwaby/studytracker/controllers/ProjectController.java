package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.DTO.ProjectDTO;
import org.biwaby.studytracker.services.interfaces.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PostMapping("/create")
    ResponseEntity<ProjectDTO> createNewProject(@RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(projectService.createNewProject(dto));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteProject(@RequestParam Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    ResponseEntity<ProjectDTO> editProject(@RequestParam Long projectId, @RequestBody ProjectDTO dto) {
        return ResponseEntity.ok(projectService.editProject(projectId, dto));
    }
}
