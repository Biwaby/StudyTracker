package org.biwaby.studytracker.services.interfaces;

import org.biwaby.studytracker.models.DTO.TagDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {

    TagDTO addTag(TagDTO dto);
    List<TagDTO> getAllTags();
    TagDTO getTagById(Long id);
    void deleteTag(Long id);
    TagDTO editTag(Long id, TagDTO dto);
}
