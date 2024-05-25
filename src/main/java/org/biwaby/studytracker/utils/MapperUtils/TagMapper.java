package org.biwaby.studytracker.utils.MapperUtils;

import org.biwaby.studytracker.models.DTO.TagDTO;
import org.biwaby.studytracker.models.Tag;
import org.springframework.stereotype.Service;

@Service
public class TagMapper {

    public TagDTO toDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());

        if (tag.getTitle() != null) {
            dto.setTitle(tag.getTitle());
        }

        return dto;
    }

    public Tag toEntity(TagDTO dto) {
        Tag tag = new Tag();
        tag.setTitle(dto.getTitle());

        return tag;
    }

    public void updateDataFromDTO(Tag tag, TagDTO dto) {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            tag.setTitle(dto.getTitle());
        }
    }
}
