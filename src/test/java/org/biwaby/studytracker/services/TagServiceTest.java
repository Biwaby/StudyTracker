package org.biwaby.studytracker.services;

import org.biwaby.studytracker.exceptions.notFoundExceptions.TagNotFoundException;
import org.biwaby.studytracker.models.dto.TagDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.implementations.TagServiceImpl;
import org.biwaby.studytracker.services.implementations.UserServiceImpl;
import org.biwaby.studytracker.services.interfaces.TagService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.biwaby.studytracker.utils.UseMockWithCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class TagServiceTest {

    private static final UserRepo userRepo = Mockito.mock(UserRepo.class);
    private static final RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
    private static final TagRepo tagRepo = Mockito.mock(TagRepo.class);
    private static final ProjectRepo projectRepo = Mockito.mock(ProjectRepo.class);
    private static final ProjectTaskRepo projectTaskRepo = Mockito.mock(ProjectTaskRepo.class);
    private static final TimerRecordRepo timerRecordRepo = Mockito.mock(TimerRecordRepo.class);

    private final TagMapper mapper = new TagMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserService userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder, tagRepo, projectRepo, projectTaskRepo, timerRecordRepo);
    private final TagService tagService = new TagServiceImpl(tagRepo, mapper, userService, roleRepo);

    @Test
    @UseMockWithCustomUser
    void addTag() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TagDTO inputDTO = new TagDTO(null, "test tag");
        TagDTO expectedDTO = new TagDTO(1L, "test tag");
        Tag savedTag = new Tag(null, sessionUser, "test tag");
        Tag expectedTag = new Tag(1L, sessionUser, "test tag");

        Mockito.when(tagRepo.save(savedTag)).thenReturn(expectedTag);
        assertEquals(expectedDTO, tagService.addTag(inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void getAllTags() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TagDTO> expectedList = List.of(
                new TagDTO(1L, "test tag 1"),
                new TagDTO(2L, "test tag 2")
        );

        List<Tag> foundList = List.of(
                new Tag(1L, sessionUser, "test tag 1"),
                new Tag(2L, sessionUser, "test tag 2")
        );

        Mockito.when(tagRepo.findAllByUser(sessionUser)).thenReturn(foundList);
        assertEquals(expectedList, tagService.getAllTags());
    }

    @Test
    @UseMockWithCustomUser
    void getTagById() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TagDTO expectedDTO = new TagDTO(1L, "test tag 1");
        Tag foundTag = new Tag(1L, sessionUser, "test tag 1");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(foundTag));
        assertEquals(expectedDTO, tagService.getTagById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getTagByNonExistingId() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getTagByIdForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Tag foundTag = new Tag(1L, otherUser, "test tag 1");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(foundTag));
        assertThrows(AccessDeniedException.class, () -> tagService.getTagById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteTag() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tag existingTag = new Tag(1L, sessionUser, "test tag");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        tagService.deleteTag(1L);
        Mockito.verify(tagRepo, Mockito.times(1)).delete(existingTag);
    }

    @Test
    @UseMockWithCustomUser
    void deleteNonExistingTag() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteTagForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Tag existingTag = new Tag(1L, otherUser, "test tag");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        assertThrows(AccessDeniedException.class, () -> tagService.deleteTag(1L));
    }

    @Test
    @UseMockWithCustomUser
    void editTag() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TagDTO inputDTO = new TagDTO(null, "super duper test tag");
        TagDTO expectedDTO = new TagDTO(1L, "super duper test tag");
        Tag existingTag = new Tag(1L, sessionUser, "test tag");
        Tag expectedTag = new Tag(1L, sessionUser, "super duper test tag");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        Mockito.when(tagRepo.save(existingTag)).thenReturn(expectedTag);
        assertEquals(expectedDTO, tagService.editTag(1L, inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void editForNonExistingTag() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TagNotFoundException.class, () -> tagService.editTag(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void editTagForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        Tag existingTag = new Tag(1L, otherUser, "test tag");

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        assertThrows(AccessDeniedException.class, () -> tagService.editTag(1L, null));
    }
}
