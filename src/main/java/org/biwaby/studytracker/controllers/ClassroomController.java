package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Classroom;
import org.biwaby.studytracker.models.DTO.ClassroomDTO;
import org.biwaby.studytracker.services.interfaces.ClassroomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    ResponseEntity<Classroom> addClassroom(@RequestBody ClassroomDTO dto) {
        return ResponseEntity.ok(classroomService.addClassroom(dto));
    }

    @GetMapping
    ResponseEntity<List<Classroom>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getALlClassrooms());
    }

    @GetMapping("/{id}")
    ResponseEntity<Classroom> getClassroomById(@PathVariable Long id) {
        return ResponseEntity.ok(classroomService.getClassroomById(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteClassroom(@PathVariable Long id) {
        if (classroomService.deleteClassroom(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editClassroom(@PathVariable Long id, @RequestBody ClassroomDTO dto) {
        if (classroomService.editClassroom(id, dto)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
