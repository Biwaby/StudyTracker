package org.biwaby.studytracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biwaby.studytracker.models.*;
import org.biwaby.studytracker.models.dto.TimerRecordDTO;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.utils.MapperUtils.ProjectMapper;
import org.biwaby.studytracker.utils.MapperUtils.ProjectTaskMapper;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.biwaby.studytracker.utils.MapperUtils.TimerRecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimerRecordControllerTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private TimerRecordRepo timerRecordRepo;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ProjectTaskRepo projectTaskRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private TimerRecordMapper timerRecordMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectTaskMapper projectTaskMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String formatedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    private final String formatedTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
    private final Date date = new SimpleDateFormat("dd-MM-yyyy").parse(formatedDate);
    private final Date time = new SimpleDateFormat("HH:mm:ss").parse(formatedTime);

    public TimerRecordControllerTest() throws ParseException {
    }

    @BeforeEach
    void setParams() {
        Role userRole = roleRepo.save(new Role(null, "USER"));
        Role adminRole = roleRepo.save(new Role(null, "ADMIN"));

        User sessionUser = new User(
                null,
                "testUser",
                passwordEncoder.encode("1234"),
                true,
                new HashSet<>(List.of(userRole))
        );
        sessionUser = userRepo.save(sessionUser);

        User otherUser = new User(
                null,
                "otherTestUser",
                passwordEncoder.encode("1234"),
                true,
                new HashSet<>(List.of(userRole))
        );
        otherUser = userRepo.save(otherUser);

        User admin = new User(
                null,
                "admin",
                passwordEncoder.encode("admin228"),
                true,
                new HashSet<>(List.of(userRole,adminRole))
        );
        admin = userRepo.save(admin);
    }

    @Test
    void addRecord() throws Exception {
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/timer/record")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void getAllRecords() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        List<TimerRecordDTO> expectedPageList = List.of(
                TimerRecordMapper.toDTO(timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record 1", time, time, date, null, null, new HashSet<>()))),
                TimerRecordMapper.toDTO(timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record 2", time, time, date, null, null, new HashSet<>())))
        );
        Page<TimerRecordDTO> expectedPage = new PageImpl<>(expectedPageList, PageRequest.of(0, 5), 2);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/timer?page=0")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void getRecordById() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        TimerRecordDTO expectedDTO = TimerRecordMapper.toDTO(timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>())));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/timer/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void getNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/timer/55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/timer/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/timer/delete?recordId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/timer/delete?recordId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/timer/delete?recordId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void editRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "super duper test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>()));
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "super duper test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/edit?recordId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void editNonExistingRecord() throws Exception {
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/edit?recordId=55")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));
        TimerRecordDTO inputDTO = new TimerRecordDTO(null, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/timer/edit?recordId=1")
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void addProjectToRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>()));
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, projectMapper.toDTO(existingProject), null, new HashSet<>());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/addProject?recordId=1&projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void addProjectToNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/addProject?recordId=55&projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addProjectToOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/addProject?recordId=1&projectId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void removeProjectFromRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Project existingProject = projectRepo.save(new Project(null, sessionUser, "test project", "test project desc", new ArrayList<>()));
        ProjectTask existingTask = projectTaskRepo.save(new ProjectTask(null, existingProject, "test task", "test task desc", false));
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, existingProject, existingTask, new HashSet<>()));
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/removeProject?recordId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void removeProjectFromNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/removeProject?recordId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void removeProjectFromOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/timer/removeProject?recordId=1")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void addTagToRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>()));
        Tag existingTag = tagRepo.save(new Tag(null, sessionUser, "test tag"));
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>(List.of(tagMapper.toDTO(existingTag))));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/addTag?recordId=1&tagId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void addTagToNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/addTag?recordId=55&tagId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addTagToOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/timer/addTag?recordId=1&tagId=1")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void removeTagFromRecord() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Tag existingTag = tagRepo.save(new Tag(null, sessionUser, "test tag"));
        timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record", time, time, date, null, null, new HashSet<>(List.of(existingTag))));
        TimerRecordDTO expectedDTO = new TimerRecordDTO(1L, "test record", formatedTime, formatedTime, formatedDate, null, null, new HashSet<>());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/timer/removeTag?recordId=1&tagId=1")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void removeTagFromNonExistingRecord() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/timer/removeTag?recordId=55&tagId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void removeTagFromOtherUserRecord() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        timerRecordRepo.save(new TimerRecord(null,  otherUser, "test record", time, time, date, null, null, new HashSet<>()));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/timer/removeTag?recordId=1&tagId=1")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getAllRecordsByUser() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        List<TimerRecord> expectedPageList = List.of(
                timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record 1", time, time, date, null, null, new HashSet<>())),
                timerRecordRepo.save(new TimerRecord(null,  sessionUser, "test record 2", time, time, date, null, null, new HashSet<>()))
        );
        Page<TimerRecord> expectedPage = new PageImpl<>(expectedPageList, PageRequest.of(0, 5), 2);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/admin/timer?username=testUser&page=0")
                                .with(httpBasic("admin", "admin228"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedPage)));
    }

    @Test
    void getAllRecordsByNonExistingUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/admin/timer?username=unknownUser&page=0")
                                .with(httpBasic("admin", "admin228"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void nonAdminGettingAllRecordsByUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/admin/timer?username=unknownUser&page=0")
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
