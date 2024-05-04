package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Classmate;
import org.biwaby.studytracker.services.interfaces.ClassmateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classmates")
@RequiredArgsConstructor
public class ClassmateController {
    private final ClassmateService classmateService;

    @PostMapping
    ResponseEntity<Classmate> addClassmate(@RequestBody Classmate classmate) {
        return ResponseEntity.ok(classmateService.addClassmate(classmate));
    }

    @GetMapping
    ResponseEntity<List<Classmate>> getAllClassmates() {
        return ResponseEntity.ok(classmateService.getAllClassmates());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteClassmate(@PathVariable Long id) {
        if (classmateService.deleteClassmate(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editClassmate(@PathVariable Long id, @RequestBody Classmate classmate) {
        if (classmateService.editClassmate(id, classmate)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
