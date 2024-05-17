package org.biwaby.studytracker.controllers;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.models.DTO.TimetableItemDTO;
import org.biwaby.studytracker.models.DTO.ViewDTO.TimetableDTO;
import org.biwaby.studytracker.models.TimetableItem;
import org.biwaby.studytracker.services.interfaces.TimetableItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timetable")
public class TimetableItemController {

    private final TimetableItemService timetableItemService;

    @PostMapping
    ResponseEntity<TimetableDTO> addItemToTimetable(@RequestBody TimetableItemDTO dto) throws ParseException {
        return ResponseEntity.ok(timetableItemService.addItemInTimetable(dto));
    }

    @GetMapping
    ResponseEntity<List<TimetableDTO>> getAllItemsFromTimetable() {
        return ResponseEntity.ok(timetableItemService.getAllItemsFromTimetable());
    }

    @GetMapping("/{id}")
    ResponseEntity<TimetableDTO> getTimetableItemById(@PathVariable Long id) {
        return ResponseEntity.ok(timetableItemService.getTimetableItemById(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTimetableItem(@PathVariable Long id) {
        timetableItemService.deleteItemFromTimetable(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> editTimetableItem(@PathVariable Long id, @RequestBody TimetableItemDTO dto) throws ParseException {
        timetableItemService.editItemInTimetable(id, dto);
        return ResponseEntity.ok().build();
    }
}
