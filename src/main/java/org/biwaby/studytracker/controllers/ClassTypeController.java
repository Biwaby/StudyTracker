package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.ClassType;
import org.biwaby.studytracker.services.interfaces.ClassTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classtypes")
public class ClassTypeController {

    private final ClassTypeService classTypeService;

    @PostMapping
    ResponseEntity<ClassType> addClassType(@RequestBody ClassType classType) {
        return ResponseEntity.ok(classTypeService.addClassType(classType));
    }

    @GetMapping
    ResponseEntity<List<ClassType>> getAllClassTypes() {
        return ResponseEntity.ok(classTypeService.getAllClassTypes());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteClassType(@PathVariable Long id) {
        if (classTypeService.deleteClassType(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editClassType(@PathVariable Long id, @RequestBody ClassType classType) {
        if (classTypeService.editClassType(id, classType)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
