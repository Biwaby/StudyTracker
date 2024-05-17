package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.Building;
import org.biwaby.studytracker.services.interfaces.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    ResponseEntity<Building> addBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.addBuilding(building));
    }

    @GetMapping
    ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @GetMapping("/{id}")
    ResponseEntity<Building> getBuildingById(@PathVariable Long id) { return ResponseEntity.ok(buildingService.getBuildingById(id)); }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        if (buildingService.deleteBuilding(id)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editBuilding(@PathVariable Long id, @RequestBody Building building) {
        if (buildingService.editBuilding(id, building)) {
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
