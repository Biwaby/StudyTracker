package org.biwaby.studytracker.services;

import org.biwaby.studytracker.exceptions.NotFoundExceptions.TimerRecordNotFoundException;
import org.biwaby.studytracker.exceptions.NotFoundExceptions.UserNotFoundException;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.DTO.ProjectDTO;
import org.biwaby.studytracker.models.DTO.TagDTO;
import org.biwaby.studytracker.models.DTO.TimerRecordDTO;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.services.implementations.TimerRecordServiceImpl;
import org.biwaby.studytracker.services.implementations.UserServiceImpl;
import org.biwaby.studytracker.services.interfaces.TimerRecordService;
import org.biwaby.studytracker.services.interfaces.UserService;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimerRecordMapper;
import org.biwaby.studytracker.utils.UseMockWithCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class TimerRecordServiceTest {

    private final static UserRepo userRepo = Mockito.mock(UserRepo.class);
    private final static RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
    private final static TagRepo tagRepo = Mockito.mock(TagRepo.class);
    private final static ProjectRepo projectRepo = Mockito.mock(ProjectRepo.class);
    private final static ProjectTaskRepo projectTaskRepo = Mockito.mock(ProjectTaskRepo.class);
    private final static TimerRecordRepo timerRecordRepo = Mockito.mock(TimerRecordRepo.class);

    private final TimerRecordMapper mapper = new TimerRecordMapper();
    private final TagMapper tagMapper = new TagMapper();
    private final ProjectMapper projectMapper = new ProjectMapper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserService userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder, tagRepo, projectRepo, projectTaskRepo, timerRecordRepo);
    private final TimerRecordService timerRecordService = new TimerRecordServiceImpl(timerRecordRepo, mapper, userService, roleRepo, userRepo, tagRepo, tagMapper, projectRepo, projectMapper);


    private final String formatedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private final String formatedTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
    private final Date date = new SimpleDateFormat("dd-MM-yyyy").parse(formatedDate);
    private final Date time = new SimpleDateFormat("HH:mm:ss").parse(formatedTime);

    public TimerRecordServiceTest() throws ParseException {
    }

    @Test
    @UseMockWithCustomUser
    void addRecord() throws ParseException {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        TimerRecord savedRecord = new TimerRecord(null, sessionUser, "test record", time, time, date, null, new HashSet<>());
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(timerRecordRepo.save(savedRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.addRecord(inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void getAllRecords() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectDTO existingProjectDTO = new ProjectDTO(1L, "test project", "test project desc");

        List<TimerRecordDTO> expectedPageList = List.of(
                new TimerRecordDTO(1L, "test record 1", formatedTime, formatedTime, formatedDate, null, new HashSet<>()),
                new TimerRecordDTO(2L, "test record 2", formatedTime, formatedTime, formatedDate, existingProjectDTO, new HashSet<>())
        );
        Page<TimerRecordDTO> expectedPage = new PageImpl<>(expectedPageList, PageRequest.of(0, 5), 2);

        List<TimerRecord> foundPageList = List.of(
                new TimerRecord(1L, sessionUser, "test record 1", time, time, date, null, new HashSet<>()),
                new TimerRecord(2L, sessionUser, "test record 2", time, time, date, existingProject, new HashSet<>())
        );
        Page<TimerRecord> foundPage = new PageImpl<>(foundPageList, PageRequest.of(0, 5), 2);

        Mockito.when(timerRecordRepo.findAllByUser(PageRequest.of(0, 5), sessionUser)).thenReturn(foundPage);
        assertEquals(expectedPage, timerRecordService.getAllRecords(0));
    }

    @Test
    @UseMockWithCustomUser
    void getRecordById() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        TimerRecord foundRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertEquals(expectedDTO, timerRecordService.getRecordById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getRecordByNonExistingId() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.getRecordById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void getRecordByIdForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.getRecordById(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteRecord() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        timerRecordService.deleteRecord(1L);
        Mockito.verify(timerRecordRepo, Mockito.times(1)).delete(existingRecord);
    }

    @Test
    @UseMockWithCustomUser
    void deleteNonExisitngRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.deleteRecord(1L));
    }

    @Test
    @UseMockWithCustomUser
    void deleteRecordForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.deleteRecord(1L));
    }

    @Test
    @UseMockWithCustomUser
    void editRecord() throws ParseException {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectDTO existingProjectDTO = new ProjectDTO(1L, "test project", "test project desc");
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "super duper test record", formatedTime, formatedTime, formatedDate, existingProjectDTO, new HashSet<>());
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, existingProject, new HashSet<>());
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "super duper test record", time, time, date, existingProject, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        Mockito.when(timerRecordRepo.save(existingRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.editRecord(1L, inputDTO));
    }

    @Test
    @UseMockWithCustomUser
    void editNonExistingRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.editRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void editRecordForOtherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.editRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void addProjectToRecord() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        ProjectDTO existingProjectDTO = new ProjectDTO(1L, "test project", "test project desc");
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, existingProjectDTO, new HashSet<>());
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, existingProject, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(timerRecordRepo.save(existingRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.addProjectToRecord(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void addProjectToNonExistingRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.addProjectToRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void addProjectForOtherUserRecord() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.addProjectToRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void removeProjectFromRecord() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project existingProject = new Project(1L, sessionUser, "test project", "test project desc", new ArrayList<>());
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, existingProject, new HashSet<>());
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        Mockito.when(projectRepo.findById(1L)).thenReturn(Optional.of(existingProject));
        Mockito.when(timerRecordRepo.save(existingRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.removeProjectFromRecord(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void removeProjectFromNonExistingRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.removeProjectFromRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void removeProjectForOtherUserRecord() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.removeProjectFromRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void addTagToRecord() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tag existingTag = new Tag(1L, sessionUser, "test tag");
        TagDTO existingTagDTO = new TagDTO(1L, "test tag");
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, Set.of(existingTagDTO));
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, Set.of(existingTag));

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        Mockito.when(timerRecordRepo.save(existingRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.addTagToRecord(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void addTagToNonExistingRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.addTagToRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void addTagForOtherUserRecord() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.addTagToRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void removeTagFromRecord() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tag existingTag = new Tag(1L, sessionUser, "test tag");
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, new HashSet<>());
        TimerRecord existingRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>(Set.of(existingTag)));
        TimerRecord expectedRecord = new TimerRecord(1L, sessionUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(existingRecord));
        Mockito.when(tagRepo.findById(1L)).thenReturn(Optional.of(existingTag));
        Mockito.when(timerRecordRepo.save(existingRecord)).thenReturn(expectedRecord);
        assertEquals(expectedDTO, timerRecordService.removeTagFromRecord(1L, 1L));
    }

    @Test
    @UseMockWithCustomUser
    void removeTagFromNonExistingRecord() {
        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TimerRecordNotFoundException.class, () -> timerRecordService.removeTagFromRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void removeTagForOtherUserRecord() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("testUser2");
        otherUser.setPassword("1234");
        otherUser.setEnabled(true);

        TimerRecord foundRecord = new TimerRecord(1L, otherUser, "test record", time, time, date, null, new HashSet<>());

        Mockito.when(roleRepo.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role(2L, "ADMIN")));
        Mockito.when(timerRecordRepo.findById(1L)).thenReturn(Optional.of(foundRecord));
        assertThrows(AccessDeniedException.class, () -> timerRecordService.removeTagFromRecord(1L, null));
    }

    @Test
    @UseMockWithCustomUser
    void getAllRecordsByUser() {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<TimerRecord> foundPageList = List.of(
                new TimerRecord(1L, sessionUser, "test record 1", time, time, date, null, new HashSet<>()),
                new TimerRecord(2L, sessionUser, "test record 2", time, time, date, null, new HashSet<>())
        );
        Page<TimerRecord> foundPage = new PageImpl<>(foundPageList);

        List<TimerRecord> expectedPageList = List.of(
                new TimerRecord(1L, sessionUser, "test record 1", time, time, date, null, new HashSet<>()),
                new TimerRecord(2L, sessionUser, "test record 2", time, time, date, null, new HashSet<>())
        );
        Page<TimerRecord> expectedPage = new PageImpl<>(expectedPageList);

        Mockito.when(userRepo.findByUsername(sessionUser.getUsername())).thenReturn(Optional.of(sessionUser));
        Mockito.when(timerRecordRepo.findAllByUser(PageRequest.of(0, 5), sessionUser)).thenReturn(foundPage);
        assertEquals(expectedPage, timerRecordService.getAllRecordsByUser(sessionUser.getUsername(), 0));
    }

    @Test
    @UseMockWithCustomUser
    void getAllRecordsByNonExistingUser() {
        Mockito.when(userRepo.findByUsername("testUser")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> timerRecordService.getAllRecordsByUser("testUser", 0));
    }
}
