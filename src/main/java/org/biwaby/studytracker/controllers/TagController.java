package org.biwaby.studytracker.controllers;

import lombok.AllArgsConstructor;
import org.biwaby.studytracker.models.DTO.TagDTO;
import org.biwaby.studytracker.services.interfaces.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{tagId}")
    ResponseEntity<TagDTO> getTagById(@PathVariable Long tagId) {
        return ResponseEntity.ok(tagService.getTagById(tagId));
    }

    @PostMapping("/add")
    ResponseEntity<TagDTO> addTag(@RequestBody TagDTO dto) {
        return ResponseEntity.ok(tagService.addTag(dto));
    }

    @DeleteMapping("/delete")
    ResponseEntity<Void> deleteTag(@RequestParam Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit")
    ResponseEntity<TagDTO> editTag(@RequestParam Long tagId, @RequestBody TagDTO dto) {
        return ResponseEntity.ok(tagService.editTag(tagId, dto));
    }
}
