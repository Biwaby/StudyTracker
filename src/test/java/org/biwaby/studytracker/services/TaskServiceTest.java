//package org.biwaby.studytracker.services;
//
//
//import org.biwaby.studytracker.models.DTO.ViewDTO.TaskPresentationDTO;
//import org.biwaby.studytracker.models.User;
//import org.biwaby.studytracker.repositories.RoleRepo;
//import org.biwaby.studytracker.repositories.UserRepo;
//import org.biwaby.studytracker.services.implementations.UserServiceImpl;
//import org.biwaby.studytracker.services.interfaces.UserService;
//import org.biwaby.studytracker.utils.UseMockWithCustomUser;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Optional;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration
//public class TaskServiceTest {
//
//    private final Date date = new Date();
//    private final String formatedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
//
//    private static final UserRepo userRepo = Mockito.mock(UserRepo.class);
//    private static final RoleRepo roleRepo = Mockito.mock(RoleRepo.class);
//    private static final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//    private final SubjectRepo subjectRepo = Mockito.mock(SubjectRepo.class);
//    private final TaskRepo taskRepo = Mockito.mock(TaskRepo.class);
//    private final UserService userService = new UserServiceImpl(userRepo, roleRepo, passwordEncoder);
//    private final TaskMapper mapper = new TaskMapper(subjectRepo, userService);
//    private final TaskPresentationMapper presMapper = new TaskPresentationMapper(userService);
//    private final TaskService taskService = new TaskServiceImpl(taskRepo, mapper, presMapper, userService, roleRepo);
//
//    @DisplayName("Проверка добавления задания")
//    @Test
//    @UseMockWithCustomUser
//    void addTask() throws ParseException {
//        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Subject existingSubject = new Subject(1L, "Test subject", 1, 1);
//
//        Task savedTask = new Task(null, sessionUser, "Test task", "Test task descr", existingSubject, false, date, date);
//        Task expectedTask = new Task(1L, sessionUser, "Test task", "Test task descr", existingSubject, false, date, date);
//        TaskDTO inputTaskDTO = new TaskDTO(null, "Test task", "Test task descr", existingSubject.getId(), false, null, formatedDate);
//        TaskPresentationDTO expectedTaskDTO = new TaskPresentationDTO(1L, sessionUser.getUsername(), "Test task", "Test task descr", existingSubject, false, null, formatedDate);
//
//        Mockito.when(subjectRepo.findById(1L)).thenReturn(Optional.ofNullable(existingSubject));
//        Mockito.when(taskRepo.save(savedTask)).thenReturn(expectedTask);
//        assertEquals(presMapper.toDTO(expectedTask), taskService.addTask(inputTaskDTO));
//    }
//}
