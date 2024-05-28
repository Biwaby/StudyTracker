package org.biwaby.studytracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.biwaby.studytracker.models.DTO.TagDTO;
import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.Tag;
import org.biwaby.studytracker.models.User;
import org.biwaby.studytracker.repositories.*;
import org.biwaby.studytracker.utils.MapperUtils.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TagControllerTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

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
    }

    @Test
    void addTag() throws Exception {
        TagDTO inputDTO = new TagDTO(null, "test tag");
        TagDTO expectedDTO = new TagDTO(1L, "test tag");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/tags/add")
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void getAllTags() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        Tag tag1 = tagRepo.save(new Tag(null, sessionUser, "test tag 1"));
        Tag tag2 = tagRepo.save(new Tag(null, sessionUser, "test tag 2"));

        List<TagDTO> tagDTOList = List.of(
                tagMapper.toDTO(tag1),
                tagMapper.toDTO(tag2)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tags")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(tagDTOList)));
    }

    @Test
    void getTagById() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        tagRepo.save(new Tag(null, sessionUser, "test tag"));
        TagDTO foundDTO = new TagDTO(1L, "test tag");

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tags/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(foundDTO)));
    }

    @Test
    void getNonExistingTag() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/tags/55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getTagForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        tagRepo.save(new Tag(null, otherUser, "test tag"));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/tags/1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteTag() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        tagRepo.save(new Tag(null, sessionUser, "test tag"));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/tags/delete?tagId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteNonExistingTag() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/tags/delete?tagId=55")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTagForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        tagRepo.save(new Tag(null, otherUser, "test tag"));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/tags/delete?tagId=1")
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void editTag() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        TagDTO inputDTO = new TagDTO(null, "super duper test tag");
        tagRepo.save(new Tag(null, sessionUser, "test tag"));
        TagDTO editedDTO = new TagDTO(1L, "super duper test tag");

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/tags/edit?tagId=1")
                                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                                .with(httpBasic("testUser", "1234"))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(editedDTO)));
    }

    @Test
    void editNonExistingTag() throws Exception {
        User sessionUser = userRepo.findByUsername("testUser").get();
        tagRepo.save(new Tag(null, sessionUser, "test tag"));
        TagDTO inputDTO = new TagDTO(null, "super duper test tag");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/tags/edit?tagId=55")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void editTagForOtherUser() throws Exception {
        User otherUser = userRepo.findByUsername("otherTestUser").get();
        tagRepo.save(new Tag(null, otherUser, "test tag"));
        TagDTO inputDTO = new TagDTO(null, "super duper test tag");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/tags/edit?tagId=1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(inputDTO))
                        .with(httpBasic("testUser", "1234"))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
