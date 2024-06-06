package org.biwaby.studytracker.services.implementations;

import lombok.RequiredArgsConstructor;
import org.biwaby.studytracker.exceptions.notFoundExceptions.TagNotFoundException;
import org.biwaby.studytracker.models.dto.TagDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.RoleRepo;
import org.biwaby.studytracker.repositories.TagRepo;
import org.biwaby.studytracker.services.interfaces.TagService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final TagMapper mapper;
    private final UserService userService;
    private final RoleRepo roleRepo;

    @Override
    public TagDTO addTag(TagDTO dto) {
        Tag tag = mapper.toEntity(dto);
        tag.setUser(userService.getUserByAuth());
        tag = tagRepo.save(tag);
        return mapper.toDTO(tag);
    }

    @Override
    public List<TagDTO> getAllTags() {
        List<TagDTO> dtos = new ArrayList<>();
        List<Tag> tags = tagRepo.findAllByUser(userService.getUserByAuth());
        tags.forEach(tag -> dtos.add(mapper.toDTO(tag)));
        return dtos;
    }

    @Override
    public TagDTO getTagById(Long id) {
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();
        Tag tag = tagRepo.findById(id).orElseThrow(TagNotFoundException::new);

        if (!user.getId().equals(tagRepo.findById(id).get().getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        return mapper.toDTO(tag);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepo.findById(id).orElseThrow(TagNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(tag.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        tagRepo.delete(tag);
    }

    @Override
    public TagDTO editTag(Long id, TagDTO dto) {
        Tag tag = tagRepo.findById(id).orElseThrow(TagNotFoundException::new);
        User user = userService.getUserByAuth();
        Role admin = roleRepo.findByAuthority("ADMIN").get();

        if (!user.getId().equals(tag.getUser().getId()) && !user.getAuthorities().contains(admin)) {
            throw new AccessDeniedException("No access");
        }

        mapper.updateDataFromDTO(tag, dto);
        tag = tagRepo.save(tag);
        return mapper.toDTO(tag);
    }
}
